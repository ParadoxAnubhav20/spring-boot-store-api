package com.anubhav.dev.store.payments;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class CheckoutRequest {
  @NotNull(message = "Cart ID is required.")
  private UUID cartId;

}
