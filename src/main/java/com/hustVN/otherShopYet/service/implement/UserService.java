package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.components.JwtTokenUtils;
import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.entity.Role;
import com.hustVN.otherShopYet.model.entity.User;
import com.hustVN.otherShopYet.repo.RoleRepository;
import com.hustVN.otherShopYet.repo.UserRepository;
import com.hustVN.otherShopYet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    @Transactional
    public User create(UserDTO dto) throws Exception {
        String phoneNumber = dto.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(
                () -> new DataNotFoundException("Role not found"));
        if(role.getName().equals("ROLE_ADMIN")){
            throw new DataIntegrityViolationException("Admin can't be created");
        }
        //convert
        User newUser =  User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .facebookAccountId(dto.getFacebookAccountId()).googleAccountId(dto.getGoogleAccountId())
                .phoneNumber(phoneNumber)
                .build();

        newUser.setRole(role);
        // check account id
        if(dto.getGoogleAccountId() == 0 && dto.getFacebookAccountId() == 0){
            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception{
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if(user.isEmpty()){
            throw new DataNotFoundException("User Not Found");
        }
        User existUser = user.get();
        if(existUser.getFacebookAccountId() == 0 && existUser.getGoogleAccountId() == 0){
            if(!passwordEncoder.matches(password, existUser.getPassword())){
                throw new BadCredentialsException("Bad Credential, Oops..");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, existUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtils.generateToken(existUser);
    }
}
