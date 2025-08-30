package com.anubhav.dev.store.controllers;

import com.anubhav.dev.store.dtos.ErrorDto;
import com.anubhav.dev.store.dtos.OrderDto;
import com.anubhav.dev.store.entities.Order;
import com.anubhav.dev.store.entities.User;
import com.anubhav.dev.store.exceptions.OrderNotFoundException;
import com.anubhav.dev.store.mappers.OrderMapper;
import com.anubhav.dev.store.repositories.OrderRepository;
import com.anubhav.dev.store.services.AuthService;
import com.anubhav.dev.store.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.AccessDeniedException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Tag(name = "Orders")
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  public List<OrderDto> getAllOrders() {
    return orderService.getAllOrders();
  }

  @GetMapping("/{orderId}")
  public OrderDto getOrder(@PathVariable("orderId") Long orderId) {
    return orderService.getOrder(orderId);
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Void> handleOrderNotFoundException() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(e.getMessage()));
  }

}
