package com.hustVN.otherShopYet.service;

import com.hustVN.otherShopYet.model.entity.Token;
import com.hustVN.otherShopYet.model.entity.User;

public interface ITokenService {
    Token genToken(User user, String token, boolean isMobile);
    Token genRefreshToken(User user, String token) throws Exception;
}
