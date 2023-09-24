package gr.jmone.movierama.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenManager {
  @Value("${movie-rama.app.secret}")
  private String secret;

  @Value("${movie-rama.app.expirationMn}")
  private Long expirationMn;

  public static final String DEFAULT_ROLE = "ROLE_USER";
  public static final String TOKEN_TYPE = "JWT";
  public static final String TOKEN_ISSUER = "movierama-api";
  public static final String TOKEN_AUDIENCE = "movierama-app";

  public String generateJwtToken(Authentication authentication) {

    var userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    var roles =
        userPrincipal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    return Jwts.builder()
        .setHeaderParam("typ", TOKEN_TYPE)
        .signWith(Keys.hmacShaKeyFor(key()), SignatureAlgorithm.HS512)
        .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expirationMn).toInstant()))
        .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
        .setId(UUID.randomUUID().toString())
        .setIssuer(TOKEN_ISSUER)
        .setAudience(TOKEN_AUDIENCE)
        .setSubject((userPrincipal.getUsername()))
        .claim("rol", roles)
        .claim("name", userPrincipal.getName())
        .claim("externalId", userPrincipal.getExternalId())
        .claim("preferred_username", userPrincipal.getUsername())
        .claim("email", userPrincipal.getEmail())
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  private byte[] key() {
    return secret.getBytes();
  }
}
