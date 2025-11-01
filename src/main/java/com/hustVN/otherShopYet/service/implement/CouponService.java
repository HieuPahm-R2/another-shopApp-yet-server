package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.model.entity.Coupon;
import com.hustVN.otherShopYet.model.entity.CouponCondition;
import com.hustVN.otherShopYet.repo.CouponConditionRepository;
import com.hustVN.otherShopYet.repo.CouponRepository;
import com.hustVN.otherShopYet.service.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService {
    private final CouponRepository couponRepository;
    private final CouponConditionRepository couponConditionRepository;

    private double calcDiscount(Coupon coupon, double totalAmount) {
        List<CouponCondition> conditions = couponConditionRepository.findByCouponId(coupon.getId());
        double discount = 0.0;
        double reTotal = totalAmount;
        for (CouponCondition condition : conditions) {
            //EAV model
            String attribute = condition.getAttribute();
            String operator = condition.getOperator();
            String value = condition.getValue();
            double percentage = Double.parseDouble(String.valueOf(condition.getDiscountAmount()));
            if (attribute.equals("minimum_amount")) {
                if(operator.equals(">") && reTotal >= Double.parseDouble(value) ){
                    discount += reTotal * percentage / 100;
                }
            } else if (attribute.equals("applicable_date")) {
                LocalDate applicableDate = LocalDate.parse(value);
                LocalDate currentDate = LocalDate.now();
                if (operator.equalsIgnoreCase("BETWEEN")
                        && currentDate.isEqual(applicableDate)) {
                    discount += reTotal * percentage / 100;
                }
            }
            reTotal -= discount;
        }
        return discount;
    }

    @Override
    public double calcCouponValue(String couponCode, double totalAmount) {
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        if (!coupon.isActive()) {
            throw new IllegalArgumentException("Coupon is not active");
        }
        double discount = calcDiscount(coupon, totalAmount);
        return totalAmount - discount;
    }


}
