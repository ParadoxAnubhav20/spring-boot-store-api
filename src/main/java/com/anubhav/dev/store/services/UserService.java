package com.anubhav.dev.store.services;

import com.anubhav.dev.store.entities.User;
import com.anubhav.dev.store.repositories.UserRepository;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
        Collections.emptyList());
  }
}
