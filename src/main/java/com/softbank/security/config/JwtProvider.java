package com.softbank.security.config;

import com.softbank.security.custom.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtProvider{

  @Value("${secret.encode-key}")
  private String KEY;

  public String generateToken(CustomUserDetails userDetails) {
    long expriedTime = 180000;
    return Jwts.builder().setSubject(userDetails.getUsername())
        .setExpiration(new Date(System.currentTimeMillis() + expriedTime))
        .setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS512, KEY)
        .compact();
  }

  public String getAccountCodeFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(KEY)
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(KEY).parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
      throw ex;
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }
}
