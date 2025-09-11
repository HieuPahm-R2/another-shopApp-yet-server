package com.hustVN.otherShopYet.service;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    User create(UserDTO dto) throws Exception;

    String login(String phoneNumber, String password) throws Exception;
}
