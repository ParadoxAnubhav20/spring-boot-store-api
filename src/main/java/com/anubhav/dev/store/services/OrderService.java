package com.anubhav.dev.store.services;

import com.anubhav.dev.store.dtos.OrderDto;
import com.anubhav.dev.store.entities.Order;
import com.anubhav.dev.store.entities.User;
import com.anubhav.dev.store.exceptions.OrderNotFoundException;
import com.anubhav.dev.store.mappers.OrderMapper;
import com.anubhav.dev.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

  private final AuthService authService;
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  public List<OrderDto> getAllOrders() {
    User user = authService.getCurrentUser();
    List<Order> orders = orderRepository.getOrdersByCustomer(user);
    return orders.stream().map(orderMapper::toOrderDto).toList();
  }

  public OrderDto getOrder(Long orderId) {
    Order order = orderRepository.getOrderWithItems(orderId).
        orElseThrow(OrderNotFoundException::new);
    User user = authService.getCurrentUser();
    if (!order.isPlacedBy(user)) {
      throw new AccessDeniedException("You do not have access to this order");
    }
    return orderMapper.toOrderDto(order);
  }
}
