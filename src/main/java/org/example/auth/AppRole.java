package org.example.auth;

import io.javalin.core.security.RouteRole;

public enum AppRole implements RouteRole {
  ANYONE,
  LOGGED_IN
}
