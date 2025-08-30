package com.anubhav.dev.store.controllers;

import com.anubhav.dev.store.config.JwtConfig;
import com.anubhav.dev.store.dtos.JwtResponse;
import com.anubhav.dev.store.dtos.LoginRequest;
import com.anubhav.dev.store.dtos.UserDto;
import com.anubhav.dev.store.entities.User;
import com.anubhav.dev.store.mappers.UserMapper;
import com.anubhav.dev.store.repositories.UserRepository;
import com.anubhav.dev.store.services.Jwt;
import com.anubhav.dev.store.services.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtConfig jwtConfig;

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest,
      HttpServletResponse response) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(), loginRequest.getPassword()
    ));
    User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

    Jwt accessToken = jwtService.generateAccessToken(user);
    Jwt refreshToken = jwtService.generateRefreshToken(user);
    Cookie cookie = new Cookie("refreshToken", refreshToken.toString());
    cookie.setHttpOnly(true);
    cookie.setPath("/auth/refresh");
    cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); //7 days
    cookie.setSecure(true);
    response.addCookie(cookie);
    return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> me() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = (Long) authentication.getPrincipal();

    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserDto userDto = userMapper.toDto(user);
    return ResponseEntity.ok(userDto);
  }

  @PostMapping("/refresh")
  public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
    Jwt jwt = jwtService.parseToken(refreshToken);
    if (jwt == null || jwt.isExpired()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Long userId = jwt.getUserId();
    User user = userRepository.findById(userId).orElseThrow();
    String accessToken = jwtService.generateAccessToken(user).toString();
    return ResponseEntity.ok(new JwtResponse(accessToken));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Void> handleBadCredentialsException() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
