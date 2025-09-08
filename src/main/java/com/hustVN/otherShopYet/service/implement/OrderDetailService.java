package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.OrderDetailDTO;
import com.hustVN.otherShopYet.model.entity.Order;
import com.hustVN.otherShopYet.model.entity.OrderDetail;
import com.hustVN.otherShopYet.model.entity.Product;
import com.hustVN.otherShopYet.repo.OrderDetailRepository;
import com.hustVN.otherShopYet.repo.OrderRepository;
import com.hustVN.otherShopYet.repo.ProductRepository;
import com.hustVN.otherShopYet.service.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO dto) throws Exception {
        Order existOrder = orderRepository.findById(dto.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Order not found"));
        Product existProduct = productRepository.findById(dto.getProductId()).orElseThrow(
                () -> new DataNotFoundException("Product not found"));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(existOrder)
                .product(existProduct)
                .totalMoney(dto.getTotalMoney())
                .numberOfProducts(dto.getNumberOfProducts())
                .price(dto.getPrice())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Order Detail not found"));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO dto) throws DataNotFoundException {
        //tìm xem order detail có tồn tại ko
        OrderDetail existOrderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Order Detail not found"));
        Order existOrder = orderRepository.findById(dto.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Order not found"));
        Product existProduct = productRepository.findById(dto.getProductId()).orElseThrow(
                () -> new DataNotFoundException("Product not found"));
        existOrderDetail.setOrder(existOrder);
        existOrderDetail.setProduct(existProduct);
        existOrderDetail.setTotalMoney(dto.getTotalMoney());
        existOrderDetail.setNumberOfProducts(dto.getNumberOfProducts());
        existOrderDetail.setPrice(dto.getPrice());
        return orderDetailRepository.save(existOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

}
