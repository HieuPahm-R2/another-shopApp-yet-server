package com.hustVN.otherShopYet.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hustVN.otherShopYet.model.dtos.OrderDetailDTO;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok("Create Done");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") long id) {
        return ResponseEntity.ok("get DONE");
    }

    @GetMapping("/order/{orderDetailId}")
    public ResponseEntity<?> getListOrderDetails(@Valid @PathVariable("orderDetailId") long id) {
        return ResponseEntity.ok("get List orderDetails DONE");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOderDetails(@Valid @PathVariable("id") long id,
            @RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok("update DONE");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetailOrder(@Valid @PathVariable("id") long id) {
        return ResponseEntity.noContent().build();
    }
}
