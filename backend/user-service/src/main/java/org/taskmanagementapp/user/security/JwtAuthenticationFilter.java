package org.taskmanagementapp.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.taskmanagementapp.user.service.JwtService;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");
    log.debug("Processing request to: {} with auth header: {}", request.getRequestURI(),
        authHeader != null ? "Bearer ***" : "null");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.debug("No valid auth header found");
      filterChain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);

    if (jwtService.isTokenValid(token)) {
      String email = jwtService.extractEmail(token);
      log.debug("Valid token for email: {}", email);

      if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            email, null, new ArrayList<>());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Authentication set for user: {}", email);
      }
    } else {
      log.debug("Invalid token provided");
    }

    filterChain.doFilter(request, response);
  }
}