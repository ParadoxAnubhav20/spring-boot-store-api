package com.anubhav.dev.store.payments;

import com.anubhav.dev.store.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {

  private Long orderId;
  private PaymentStatus paymentStatus;

}
