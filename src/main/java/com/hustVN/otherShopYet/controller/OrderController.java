package com.hustVN.otherShopYet.controller;

import com.hustVN.otherShopYet.model.entity.Order;
import com.hustVN.otherShopYet.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.hustVN.otherShopYet.model.dtos.OrderDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> res = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(res);
            }

            return ResponseEntity.ok( orderService.createOrder(orderDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getListOrdersOfUser(@Valid @PathVariable("user_id") long id) {
        try {
            List<Order> orders = orderService.findByUserId(id);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@Valid @PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(orderService.getOrder(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") long id,
            @RequestBody OrderDTO orderDTO) {
        try {
            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Delete Done");
    }
}
