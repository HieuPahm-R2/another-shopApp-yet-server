package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.exception.DataNotFoundException;
import com.hustVN.otherShopYet.model.dtos.OrderDTO;
import com.hustVN.otherShopYet.model.entity.Order;
import com.hustVN.otherShopYet.model.entity.OrderStatus;
import com.hustVN.otherShopYet.model.entity.User;
import com.hustVN.otherShopYet.repo.OrderRepository;
import com.hustVN.otherShopYet.repo.UserRepository;
import com.hustVN.otherShopYet.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new DataNotFoundException("User not found"));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.PENDING);
        // shipping date >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null
                ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today !");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        //EAV-Entity-Attribute-Value model
        order.setTotalMoney(orderDTO.getTotalMoney());
       return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not Found With Id: " + id ));
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("Not Found User"));
        Order order = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Not Found Order"));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // update các trường của đơn hàng từ orderDTO
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Page<Order> getOrdersByKeyword(String keyword, Pageable pageable) {
        return null;
    }
}
