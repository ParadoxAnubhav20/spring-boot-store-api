package com.anubhav.dev.store.controllers;

import com.anubhav.dev.store.dtos.AddItemToCartRequest;
import com.anubhav.dev.store.dtos.CartDto;
import com.anubhav.dev.store.dtos.CartItemDto;
import com.anubhav.dev.store.dtos.UpdateCartItemRequest;
import com.anubhav.dev.store.exceptions.CartNotFoundException;
import com.anubhav.dev.store.exceptions.ProductNotFoundException;
import com.anubhav.dev.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Carts")
public class CartController {

  private final CartService cartService;

  @PostMapping
  public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
    CartDto cartDto = cartService.createCart();
    URI uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
    return ResponseEntity.created(uri).body(cartDto);
  }

  @PostMapping("/{cartId}/items")
  public ResponseEntity<CartItemDto> addToCart(@PathVariable UUID cartId,
      @RequestBody AddItemToCartRequest request) {
    CartItemDto cartItemDto = cartService.addToCart(cartId, request.getProductId());
    return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
  }

  @GetMapping("/{cartId}")
  public CartDto getCart(@PathVariable UUID cartId) {
    return cartService.getCart(cartId);
  }

  @PutMapping("/{cartId}/items/{productId}")
  public CartItemDto updateItem(@PathVariable("cartId") UUID cartId,
      @PathVariable("productId") Long productId, @Valid @RequestBody UpdateCartItemRequest request) {
    return cartService.updateItem(cartId, productId, request.getQuantity());
  }

  @DeleteMapping("/{cartId}/items/{productId}")
  @Operation(summary = "Remove a product from the cart")
  public ResponseEntity<?> removeItem(@PathVariable("cartId") UUID cartId,
      @PathVariable("productId") Long productId) {
    cartService.removeItem(cartId, productId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{cartId}/items")
  @Operation(summary = "Clear all products from the cart")
  public ResponseEntity<?> clearCart(@PathVariable("cartId") UUID cartId) {
    cartService.clearCart(cartId);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(CartNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleCartNotFound() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart Not Found"));
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleProductNotFund() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product Not Found In The Cart"));
  }

}
