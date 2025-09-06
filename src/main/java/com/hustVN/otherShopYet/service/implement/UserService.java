package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.entity.Role;
import com.hustVN.otherShopYet.model.entity.User;
import com.hustVN.otherShopYet.repo.RoleRepository;
import com.hustVN.otherShopYet.repo.UserRepository;
import com.hustVN.otherShopYet.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User create(UserDTO dto) throws DataNotFoundException {
        String phoneNumber = dto.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
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
        Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(
                () -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);
        // check account id
        if(dto.getGoogleAccountId() == 0 && dto.getFacebookAccountId() == 0){
            String password = dto.getPassword();
            newUser.setPassword(password);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        return "";
    }
}
