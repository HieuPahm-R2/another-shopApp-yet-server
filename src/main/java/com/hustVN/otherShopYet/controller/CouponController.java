package com.hustVN.otherShopYet.controller;

import com.hustVN.otherShopYet.response.CouponResponse;
import com.hustVN.otherShopYet.service.implement.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/coupons")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/calculate")
    public ResponseEntity<CouponResponse> calculateCouponValue(
            @RequestParam("couponCode") String couponCode,
            @RequestParam("totalAmount") double totalAmount) {
        try {
            double finalAmount = couponService.calcCouponValue(couponCode, totalAmount);
            CouponResponse response = CouponResponse.builder()
                    .result(finalAmount)
                    .errorMessage("")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    CouponResponse.builder()
                            .result(totalAmount)
                            .errorMessage(e.getMessage())
                            .build());
        }
    }

}
