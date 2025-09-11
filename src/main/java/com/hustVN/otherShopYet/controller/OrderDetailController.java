package com.hustVN.otherShopYet.controller;

import com.hustVN.otherShopYet.components.LocalizationUtils;
import com.hustVN.otherShopYet.model.entity.OrderDetail;
import com.hustVN.otherShopYet.response.MessageKey;
import com.hustVN.otherShopYet.response.OrderDetailResponse;
import com.hustVN.otherShopYet.service.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hustVN.otherShopYet.model.dtos.OrderDetailDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try{
            OrderDetail res = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.from(res));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(orderDetailService.getOrderDetail(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderDetailId}")
    public ResponseEntity<?> getListOrderDetails(@Valid @PathVariable("orderDetailId") long id) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(id);
        List<OrderDetailResponse> res = orderDetails.stream().map(OrderDetailResponse::from).toList();
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOderDetails(@Valid @PathVariable("id") long id,
            @RequestBody OrderDetailDTO dto) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, dto);
            return ResponseEntity.ok(orderDetail);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDetailOrder(@Valid @PathVariable("id") long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok().body(localizationUtils
                .getLocalizedMessage(MessageKey.DELETE_ORDER_DETAIL_SUCCESSFULLY));
    }
}
