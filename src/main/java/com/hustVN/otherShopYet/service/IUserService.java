package com.hustVN.otherShopYet.service;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.UpdateUserDTO;
import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    User create(UserDTO dto) throws Exception;

    String login(String phoneNumber, String password) throws Exception;

    User getUserDetailsFromToken(String token) throws Exception;

    User getUserDetailFromRefreshToken(String token) throws Exception;

    User updateUser(Long userId, UpdateUserDTO updateUserDTO) throws Exception;

    User findByEmail(String email) throws Exception;

    Page<User> getAllUsers(String keyword,  Pageable pageable) throws Exception;

    void blockOrEnableUser(Long userId, Boolean isBlocked) throws Exception;
}
