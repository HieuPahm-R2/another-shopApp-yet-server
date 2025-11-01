package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.components.JwtTokenUtils;
import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.exception.ExpiredTokenException;
import com.hustVN.otherShopYet.model.entity.Token;
import com.hustVN.otherShopYet.model.entity.User;
import com.hustVN.otherShopYet.repo.TokenRepository;
import com.hustVN.otherShopYet.service.ITokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtil;
    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration}")
    private int expiration; // save to an environment variable
    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Transactional
    @Override
    public Token genToken(User user, String token, boolean isMobile) {
        List<Token> tokensList = tokenRepository.findByUser(user);
        if (tokensList.size() >= MAX_TOKENS) {
            boolean hasNonMobile = !tokensList.stream().allMatch(Token::isMobile);
            Token toDelete = hasNonMobile
                    ? tokensList.stream().filter(Token::isMobile).findFirst().orElse(tokensList.get(0))
                    : tokensList.get(0);
            tokenRepository.delete(toDelete);
        }
        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
        // gen new token for user
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobile)
                .build();
        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    public Token genRefreshToken(User user, String token) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(token);
        if (existingToken == null) {
            throw new DataNotFoundException("Refresh token does not exist");
        }
        if (existingToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(existingToken);
            throw new ExpiredTokenException("Refresh token is expired");
        }
        String tokenNew = jwtTokenUtil.generateToken(user);
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        existingToken.setExpirationDate(expirationDateTime);
        existingToken.setToken(tokenNew);
        existingToken.setRefreshToken(UUID.randomUUID().toString());
        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        return existingToken;
    }
}
