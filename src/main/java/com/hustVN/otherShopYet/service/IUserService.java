package com.hustVN.otherShopYet.service;

import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.entity.User;

public interface IUserService {
    User create(UserDTO dto);

    String login(String phoneNumber, String password);
}
