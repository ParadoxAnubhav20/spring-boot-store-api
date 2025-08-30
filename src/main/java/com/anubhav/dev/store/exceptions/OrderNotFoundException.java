package com.anubhav.dev.store.exceptions;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException() {
    super("Order Not Found");
  }
}
