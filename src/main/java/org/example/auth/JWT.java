package org.example.auth;

import io.javalin.http.Context;
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

  public static void setSession(Context ctx, Long userId) {
    String session = Jwts.builder()
        .setSubject(userId.toString())
        .signWith(privateKey, SignatureAlgorithm.HS256)
        .setExpiration(date(LocalDate.now().plusMonths(6)))
        .compact();

    ctx.cookie("session", session);
  }

  public static Long getSession(Context ctx) {
    String session = ctx.cookie("session");
    if (session == null) return null;
    try {
      String userId = Jwts.parserBuilder()
          .setSigningKey(privateKey)
          .build()
          .parseClaimsJws(session)
          .getBody()
          .getSubject();

      return Long.valueOf(userId);
    } catch (JwtException | NumberFormatException | NullPointerException e) {
      return null;
    }
  }

  public static void clearSession(Context ctx) {
    ctx.removeCookie("session");
  }

  private static Date date(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
  }
}
