package org.example.auth;

import io.javalin.core.security.RouteRole;

public enum SessionRole implements RouteRole {
  ANYONE,
  LOGGED_IN,
  NOT_LOGGED_IN,
}
