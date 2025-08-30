package com.anubhav.dev.store.payments;


import com.anubhav.dev.store.dtos.ErrorDto;
import com.anubhav.dev.store.exceptions.CartEmptyException;
import com.anubhav.dev.store.exceptions.CartNotFoundException;
import com.anubhav.dev.store.repositories.OrderRepository;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

  private final CheckoutService checkoutService;
  private final OrderRepository orderRepository;


  @PostMapping
  public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
    return checkoutService.checkout(checkoutRequest);
  }

  @PostMapping("/webhook")
  public void handleWebhook(@RequestHeader Map<String, String> headers,
      @RequestBody String payload) {
    checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
  }

  @ExceptionHandler(PaymentException.class)
  public ResponseEntity<ErrorDto> handlePaymentException(PaymentException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
        body(new ErrorDto("Internal Server Error"));
  }

  @ExceptionHandler({CartEmptyException.class, CartNotFoundException.class})
  public ResponseEntity<ErrorDto> handleException(Exception exception) {
    return ResponseEntity.badRequest().body(new ErrorDto(exception.getMessage()));
  }

}
