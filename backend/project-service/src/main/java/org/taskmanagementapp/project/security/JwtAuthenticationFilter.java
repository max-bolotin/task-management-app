package org.taskmanagementapp.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKey;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

  @ConfigProperty(name = "jwt.secret", defaultValue = "myVerySecretKeyForJWTTokenGeneration123456789")
  String jwtSecret;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    String path = requestContext.getUriInfo().getPath();

    // Skip JWT validation for health check and OpenAPI endpoints
    if (path.startsWith("q/") || path.equals("health")) {
      return;
    }

    String authHeader = requestContext.getHeaderString("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("{\"message\":\"Missing or invalid Authorization header\"}")
          .build());
      return;
    }

    String token = authHeader.substring(7);

    try {
      SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
      Claims claims = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      // Store user ID in request context for use in services
      Long userId = claims.get("userId", Long.class);
      requestContext.setProperty("userId", userId);
      requestContext.setProperty("userEmail", claims.getSubject());

    } catch (Exception e) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("{\"message\":\"Invalid JWT token\"}")
          .build());
    }
  }
}