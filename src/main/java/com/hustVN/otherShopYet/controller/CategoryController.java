package com.hustVN.otherShopYet.controller;

import com.hustVN.otherShopYet.domain.dtos.CategoryDTO;
import jakarta.validation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @GetMapping("")
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("get categories done");
    }
    @PostMapping("")
    public ResponseEntity<?> insertCategory(
            @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
       try{
           if (result.hasErrors()) {
               List<String> res = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
               return ResponseEntity.badRequest().body(res);
           }
           return ResponseEntity.ok("get categories done" + categoryDTO);
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") long id) {
        return ResponseEntity.ok("get categories done");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") long id) {
        return ResponseEntity.ok("get categories done");
    }
}
