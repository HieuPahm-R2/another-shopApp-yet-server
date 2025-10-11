package com.hustVN.otherShopYet.controller;

import com.hustVN.otherShopYet.components.LocalizationUtils;
import com.hustVN.otherShopYet.model.dtos.UpdateUserDTO;
import com.hustVN.otherShopYet.model.entity.User;
import com.hustVN.otherShopYet.response.LoginResponse;
import com.hustVN.otherShopYet.response.MessageKey;
import com.hustVN.otherShopYet.response.RegisterResponse;
import com.hustVN.otherShopYet.response.UserResponse;
import com.hustVN.otherShopYet.service.IUserService;
import com.hustVN.otherShopYet.service.implement.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.dtos.UserLoginDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;

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
    public ResponseEntity<?> loginAction(@Valid @RequestBody UserLoginDTO dto) {
        try {
            String token = userService.login(
                    dto.getPhoneNumber(), dto.getPassword());
            User user = this.userService.getUserDetailsFromToken(token);
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_SUCCESSFULLY))
                    .token(token)
                    .username(user.getFullName())
                    .id(user.getId()).build()
            );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKey.LOGIN_FAILED,e.getMessage()))
                    .build());
        }
    }
    @PostMapping("/details")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authorizationHeader){
        try {
            String tokenWithoutBearer = authorizationHeader.replace("Bearer ", "");
            User user = this.userService.getUserDetailsFromToken(tokenWithoutBearer);
            return ResponseEntity.ok(UserResponse.from(user));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUser(
            @PathVariable long id,
            @RequestBody UpdateUserDTO updateUserDTO,
            @RequestHeader("Authorization") String authorizationHeader){
        try{
            String tokenWithoutBearer = authorizationHeader.replace("Bearer ", "");
            User user = this.userService.getUserDetailsFromToken(tokenWithoutBearer);
            if(user.getId() != id){
                return ResponseEntity.status(403).body("You don't have permission to update this user");
            }
            User updatedUser = userService.updateUser(id, updateUserDTO);
            return ResponseEntity.ok(UserResponse.from(updatedUser));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
