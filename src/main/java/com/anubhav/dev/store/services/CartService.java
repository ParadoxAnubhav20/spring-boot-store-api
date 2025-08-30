package com.anubhav.dev.store.services;

import com.anubhav.dev.store.dtos.CartDto;
import com.anubhav.dev.store.dtos.CartItemDto;
import com.anubhav.dev.store.entities.Cart;
import com.anubhav.dev.store.entities.CartItem;
import com.anubhav.dev.store.entities.Product;
import com.anubhav.dev.store.exceptions.CartNotFoundException;
import com.anubhav.dev.store.exceptions.ProductNotFoundException;
import com.anubhav.dev.store.mappers.CartMapper;
import com.anubhav.dev.store.repositories.CartRepository;
import com.anubhav.dev.store.repositories.ProductRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {

  private CartRepository cartRepository;
  private CartMapper cartMapper;
  private ProductRepository productRepository;

  public CartDto createCart() {
    Cart cart = new Cart();
    cartRepository.save(cart);
    return cartMapper.toDto(cart);
  }

  public CartItemDto addToCart(UUID cartId, Long productId) {
    Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    Product product = productRepository.findById(productId).orElse(null);
    if (product == null) {
      throw new ProductNotFoundException();
    }
    CartItem cartItem = cart.addItem(product);
    cartRepository.save(cart);
    return cartMapper.toDto(cartItem);
  }

  public CartDto getCart(UUID cartId) {
    Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    return cartMapper.toDto(cart);
  }

  public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity) {
    Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    CartItem cartItem = cart.getItem(productId);
    if (cartItem == null) {
      throw new ProductNotFoundException();
    }
    cartItem.setQuantity(quantity);
    cartRepository.save(cart);
    return cartMapper.toDto(cartItem);
  }

  public void removeItem(UUID cartId, Long productId) {
    Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    CartItem cartItem = cart.getItem(productId);
    if (cartItem == null) {
      throw new ProductNotFoundException();
    }
    cart.removeItem(productId);
    cartRepository.save(cart);
  }

  public void clearCart(UUID cartId) {
    Cart cart = cartRepository.getCartWithItems(cartId).orElse(null);
    if (cart == null) {
      throw new CartNotFoundException();
    }
    cart.clear();
    cartRepository.save(cart);
  }
}
