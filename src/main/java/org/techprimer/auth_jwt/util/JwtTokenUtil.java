package org.techprimer.auth_jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {
  private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  @Value("${jwt.secret}")
  private String secret;

  public String getUsername(String token) {
    return getClaimToken(token, Claims::getSubject);
  }

  public Date getExpiration(String token) {
    return getClaimToken(token, Claims::getExpiration);
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpiration(token);
    return expiration.before(new Date());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public <T> T getClaimToken(String token, Function<Claims, T> resolver){
    final Claims claims = getAllClaimsToken(token);
    return resolver.apply(claims);
  }

  private Claims getAllClaimsToken(String token) {
    return Jwts
        .parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody();
  }

}
