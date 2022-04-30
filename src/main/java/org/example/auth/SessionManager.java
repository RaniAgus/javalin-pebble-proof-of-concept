package org.example.auth;

import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SessionManager implements AccessManager {
  @Override
  public void manage(@NotNull Handler handler,
                     @NotNull Context ctx,
                     @NotNull Set<RouteRole> roles) throws Exception {
    if (roles.contains(SessionRole.NOT_LOGGED_IN)) {
      SessionRole.NOT_LOGGED_IN.handle(handler, ctx);
    } else if (roles.contains(SessionRole.LOGGED_IN)) {
      SessionRole.LOGGED_IN.handle(handler, ctx);
    } else {
      SessionRole.ANYONE.handle(handler, ctx);
    }
  }
}
