package com.hustVN.otherShopYet.controller;

import com.hustVN.otherShopYet.components.LocalizationUtils;
import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.RefreshTokenDTO;
import com.hustVN.otherShopYet.model.dtos.UpdateUserDTO;
import com.hustVN.otherShopYet.model.entity.Token;
import com.hustVN.otherShopYet.model.entity.User;
import com.hustVN.otherShopYet.response.*;
import com.hustVN.otherShopYet.service.ITokenService;
import com.hustVN.otherShopYet.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.dtos.UserLoginDTO;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final IUserService userService;
    private final ITokenService tokenService;
    private final LocalizationUtils localizationUtils;

    private boolean isMobileDevice(String userAgent) {
        // Kiểm tra User-Agent header để xác định thiết bị di động
        return userAgent.toLowerCase().contains("mobile");
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerAction(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        RegisterResponse registerResponse = new RegisterResponse();
        if (result.hasErrors()) {
            List<String> res = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            registerResponse.setMessage(res.toString());
            return ResponseEntity.badRequest().body(registerResponse);
        }
        // handle password matching with retype
        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            registerResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKey.PASSWORD_NOT_MATCH));
            return ResponseEntity.badRequest().body(registerResponse);
        }
        try {
            User user = userService.create(userDTO);
            registerResponse.setMessage("Đăng ký tài khoản thành công");
            registerResponse.setUser(user);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(registerResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAction(@Valid @RequestBody UserLoginDTO dto, HttpServletRequest request) {
        try {
            String token = userService.login(dto.getPhoneNumber(), dto.getPassword());
            // check
            String userAgent = request.getHeader("User-Agent");
            User user = this.userService.getUserDetailsFromToken(token);
            Token jwtToken = tokenService.genToken(user, token, isMobileDevice(userAgent));
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY))
                    .token(jwtToken.getToken())
                    .refreshToken(jwtToken.getRefreshToken())
                    .username(user.getFullName())
                    .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .id(user.getId())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_FAILED, e.getMessage()))
                    .build());
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            User userDetail = userService.getUserDetailFromRefreshToken(refreshTokenDTO.getRefreshToken());
            Token jwtToken = tokenService.genRefreshToken(userDetail, refreshTokenDTO.getRefreshToken());
            return ResponseEntity.ok(LoginResponse.builder()
                    .message("Refresh token successfully")
                    .token(jwtToken.getToken())
                    .tokenType(jwtToken.getTokenType())
                    .refreshToken(jwtToken.getRefreshToken())
                    .username(userDetail.getUsername())
                    .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .id(userDetail.getId())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_FAILED,
                                    e.getMessage()))
                            .build());
        }
    }

    @PostMapping("/details")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String tokenWithoutBearer = authorizationHeader.replace("Bearer ", "");
            User user = this.userService.getUserDetailsFromToken(tokenWithoutBearer);
            return ResponseEntity.ok(UserResponse.from(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUser(
            @PathVariable long id,
            @RequestBody UpdateUserDTO updateUserDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String tokenWithoutBearer = authorizationHeader.replace("Bearer ", "");
            User user = this.userService.getUserDetailsFromToken(tokenWithoutBearer);
            if (user.getId() != id) {
                return ResponseEntity.status(403).body("You don't have permission to update this user");
            }
            User updatedUser = userService.updateUser(id, updateUserDTO);
            return ResponseEntity.ok(UserResponse.from(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // reset password Or forgot password feature
//    @PutMapping("/reset-password")
//    public ResponseEntity<?> forgotPassword(HttpServletRequest request,
//     @RequestBody HashMap<String, String> mapper) throws Exception{
//        User user = this.userService.findByEmail(mapper.get("email"));
//
//    }

    // block/banned User
    @PutMapping("/block/{userId}/{active}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> blockOrEnable(
            @Valid @PathVariable long userId,
            @Valid @PathVariable int active) {
        try {
            userService.blockOrEnableUser(userId, active > 0);
            String message = active > 0 ? "Successfully enabled the user." : "Successfully blocked the " + "user.";
            return ResponseEntity.ok().body(message);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "", required = false) String keyword,
                                         @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit) {
        try {
            PageRequest pageRequest = PageRequest.of(page, limit,  Sort.by("id").ascending());
            Page<UserResponse> users = userService.getAllUsers(keyword, pageRequest)
                    .map(UserResponse::from);
            int totalPages = users.getTotalPages();
            List<UserResponse> userResponses = users.getContent();
            return ResponseEntity.ok(UserListResponse
                    .builder()
                    .users(userResponses)
                    .totalPages(totalPages)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
