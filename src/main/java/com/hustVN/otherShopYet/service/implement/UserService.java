package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.components.JwtTokenUtils;
import com.hustVN.otherShopYet.components.LocalizationUtils;
import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.exception.ExpiredTokenException;
import com.hustVN.otherShopYet.model.dtos.UpdateUserDTO;
import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.entity.Role;
import com.hustVN.otherShopYet.model.entity.Token;
import com.hustVN.otherShopYet.model.entity.User;
import com.hustVN.otherShopYet.repo.RoleRepository;
import com.hustVN.otherShopYet.repo.TokenRepository;
import com.hustVN.otherShopYet.repo.UserRepository;
import com.hustVN.otherShopYet.response.MessageKey;
import com.hustVN.otherShopYet.service.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final LocalizationUtils localizationUtils;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public User create(UserDTO dto) throws Exception {
        String phoneNumber = dto.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(
                () -> new DataNotFoundException("Role not found"));
        if (role.getName().equals("ROLE_ADMIN")) {
            throw new DataIntegrityViolationException("Admin can't be created");
        }
        // convert
        User newUser = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .facebookAccountId(dto.getFacebookAccountId()).googleAccountId(dto.getGoogleAccountId())
                .phoneNumber(phoneNumber)
                .build();

        newUser.setRole(role);
        // check account id
        if (dto.getGoogleAccountId() == 0 && dto.getFacebookAccountId() == 0) {
            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isEmpty()) {
            throw new DataNotFoundException("User Not Found");
        }
        User existUser = user.get();
        if (existUser.getFacebookAccountId() == 0 && existUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existUser.getPassword())) {
                throw new BadCredentialsException(
                        localizationUtils.getLocalizedMessage(MessageKey.WRONG_PHONE_PASSWORD));
            }
        }
        if (!user.get().isActive()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKey.USER_IS_LOCKED));
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, existUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtils.generateToken(existUser);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token expired");
        }
        String phone = jwtTokenUtils.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phone);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("User not found");
        }
    }

    @Override
    public User getUserDetailFromRefreshToken(String token) throws Exception {
        Token reToken = tokenRepository.findByRefreshToken(token);
        return getUserDetailsFromToken(reToken.getToken());
    }

    @Override
    public User updateUser(Long userId, UpdateUserDTO updateUserDTO) throws Exception {
        User existUser = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User not found"));
        String phoneNumber = updateUserDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber) && !phoneNumber.equals(existUser.getPhoneNumber())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        if (updateUserDTO.getFullName() != null) {
            existUser.setFullName(updateUserDTO.getFullName());
        }
        if (phoneNumber != null) {
            existUser.setPhoneNumber(phoneNumber);
        }
        if (updateUserDTO.getAddress() != null) {
            existUser.setAddress(updateUserDTO.getAddress());
        }
        if (updateUserDTO.getDateOfBirth() != null) {
            existUser.setDateOfBirth(updateUserDTO.getDateOfBirth());
        }
        if (updateUserDTO.getFacebookAccountId() > 0) {
            existUser.setFacebookAccountId(updateUserDTO.getFacebookAccountId());
        }
        if (updateUserDTO.getGoogleAccountId() > 0) {
            existUser.setGoogleAccountId(updateUserDTO.getGoogleAccountId());
        }
        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            if (!updateUserDTO.getPassword().equals(updateUserDTO.getRetypePassword())) {
                throw new DataIntegrityViolationException("Passwords don't match");
            }
            String newPassword = passwordEncoder.encode(updateUserDTO.getPassword());
            existUser.setPassword(newPassword);
        }
        return userRepository.save(existUser);
    }

    @Override
    public User findByEmail(String email) throws Exception {
        if (this.userRepository.findByEmail(email).isPresent()) {
            return this.userRepository.findByEmail(email).get();
        }
        return null;
    }

    @Override
    public Page<User> getAllUsers(String keyword, Pageable pageable) throws Exception {
        return userRepository.findAll(keyword, pageable);
    }

    @Override
    public void blockOrEnableUser(Long userId, Boolean isBlocked) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        existingUser.setActive(isBlocked);
        userRepository.save(existingUser);
    }
}
