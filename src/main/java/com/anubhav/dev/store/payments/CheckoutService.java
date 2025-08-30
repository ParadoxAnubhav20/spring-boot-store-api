package com.anubhav.dev.store.payments;

import com.anubhav.dev.store.entities.Cart;
import com.anubhav.dev.store.entities.Order;
import com.anubhav.dev.store.exceptions.CartEmptyException;
import com.anubhav.dev.store.exceptions.CartNotFoundException;
import com.anubhav.dev.store.repositories.CartRepository;
import com.anubhav.dev.store.repositories.OrderRepository;
import com.anubhav.dev.store.services.AuthService;
import com.anubhav.dev.store.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

  private final CartRepository cartRepository;
  private final OrderRepository orderRepository;
  private final AuthService authService;
  private final CartService cartService;
  private final PaymentGateway paymentGateway;

  @Transactional
  public CheckoutResponse checkout(CheckoutRequest checkoutRequest) {
    Cart cart = cartRepository.getCartWithItems(checkoutRequest.getCartId()).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    if (cart.isEmpty()) {
      throw new CartEmptyException();
    }
    Order order = Order.fromCart(cart, authService.getCurrentUser());
    orderRepository.save(order);
    try {
      CheckoutSession session = paymentGateway.createCheckoutSession(order);
      cartService.clearCart(cart.getId());
      return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
    } catch (PaymentException ex) {
      orderRepository.delete(order);
      throw ex;
    }
  }

  public void handleWebhookEvent(WebhookRequest request) {
    paymentGateway
        .parseWebhookRequest(request)
        .ifPresent(paymentResult -> {
          Order order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
          order.setStatus(paymentResult.getPaymentStatus());
          orderRepository.save(order);
        });
  }
}
