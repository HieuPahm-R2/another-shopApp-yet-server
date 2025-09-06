package com.hustVN.otherShopYet.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.hustVN.otherShopYet.model.dtos.UserDTO;
import com.hustVN.otherShopYet.model.dtos.UserLoginDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<?> registerAction(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> res = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(res);
            }
            // handle password matching with retype pass
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("password is not matching...try again");
            }
            return ResponseEntity.ok("Create new account done");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAction(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok("login done");
    }
}
