package com.anubhav.dev.store.controllers;

import com.anubhav.dev.store.dtos.ChangePasswordRequest;
import com.anubhav.dev.store.dtos.RegisterUserRequest;
import com.anubhav.dev.store.dtos.UpdateUserRequest;
import com.anubhav.dev.store.dtos.UserDto;
import com.anubhav.dev.store.entities.Role;
import com.anubhav.dev.store.entities.User;
import com.anubhav.dev.store.mappers.UserMapper;
import com.anubhav.dev.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @GetMapping
  public List<UserDto> getAllUsers(@RequestHeader(required = false, name = "x-auth-token") String authToken,
      @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) {
    if (!Set.of("name", "email").contains(sortBy)) {
      sortBy = "name";
    }
    return userRepository.findAll(Sort.by(sortBy)).stream().map(userMapper::toDto).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable long id) {
    User user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(userMapper.toDto(user));
  }

  //MethodArgumentNotValidException - if POST Reqeust argument is not valid
  @PostMapping
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest,
      UriComponentsBuilder uriComponentsBuilder) {
    if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
      // Early return if email already exists
      return ResponseEntity.badRequest().body(Map.of(
          "email", "Email is already registered"
      ));
    }
    // Map request -> entity -> save -> map back to DTO
    User user = userMapper.toEntity(registerUserRequest);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);
    userRepository.save(user);
    UserDto userDto = userMapper.toDto(user);
    URI uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
    return ResponseEntity.created(uri).body(userDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id,
      @RequestBody UpdateUserRequest updateUserRequest) {
    User user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    userMapper.update(updateUserRequest, user);
    userRepository.save(user);
    return ResponseEntity.ok(userMapper.toDto(user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
    User user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    userRepository.delete(user);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/change-password")
  public ResponseEntity<Void> changePassword(@PathVariable(name = "id") Long id,
      @RequestBody ChangePasswordRequest changePasswordRequest) {

    User user = userRepository.findById(id).orElse(null);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    if (!user.getPassword().equals(changePasswordRequest.getOldPassword())) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    user.setPassword(changePasswordRequest.getNewPassword());
    userRepository.save(user);
    return ResponseEntity.noContent().build();
  }
}
