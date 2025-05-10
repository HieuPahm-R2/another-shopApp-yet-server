package com.hustVN.otherShopYet.controller;

import com.hustVN.otherShopYet.domain.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping("")
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> res = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(res);
            }

            return ResponseEntity.ok("Create new order done");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{user_id}")
    public ResponseEntity<?> getListOrders(@Valid @PathVariable("user_id") long id){
        try{
            return  ResponseEntity.ok("get List orders By userId done");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") long id,
                                         @RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok("Update Done");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") long id){
        return ResponseEntity.ok("Delete Done");
    }
}
