package org.example.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public abstract class JWT {
  private static final SecretKey privateKey = Keys.hmacShaKeyFor(
      System.getenv("APP_SECRET").getBytes(StandardCharsets.UTF_8)
  );

  public static String sign(Long userId) {
    return Jwts.builder()
        .setSubject(userId.toString())
        .signWith(privateKey, SignatureAlgorithm.HS256)
        .setExpiration(getDateFrom(LocalDate.now().plusMonths(6)))
        .compact();
  }

  public static Long verify(String jwtString) {
    if (jwtString == null) return null;
    try {
      String userId = Jwts.parserBuilder()
          .setSigningKey(privateKey)
          .build()
          .parseClaimsJws(jwtString)
          .getBody()
          .getSubject();

      return Long.valueOf(userId);
    } catch (JwtException | NumberFormatException | NullPointerException e) {
      return null;
    }
  }

  private static Date getDateFrom(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
  }
}
