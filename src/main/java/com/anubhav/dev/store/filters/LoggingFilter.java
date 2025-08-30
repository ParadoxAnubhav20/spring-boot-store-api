package com.anubhav.dev.store.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    System.out.println("Request URI: " + request.getRequestURI());
    filterChain.doFilter(request, response);
    System.out.println("Response URI: " + request.getRequestURI());
    System.out.println("Response Headers: " + request.getHeaderNames());
    System.out.println("Response:" + response.getStatus());
  }
}
