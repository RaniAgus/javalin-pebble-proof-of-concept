package org.example.data;

import io.javalin.core.security.RouteRole;

public enum Role implements RouteRole {
  ADMIN,
  STANDARD_USER
}
