package org.example;

import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.core.util.Header;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SessionManager implements AccessManager {
  @Override
  public void manage(@NotNull Handler handler,
                     @NotNull Context ctx,
                     @NotNull Set<RouteRole> routeRoles) throws Exception {
    if (routeRoles.contains(AppRole.ANYONE)) {
      handler.handle(ctx);
    }
    else if (routeRoles.contains(AppRole.LOGGED_IN) && anyUsernameProvided(ctx)) {
      handler.handle(ctx);
    }
    else {
      ctx.status(401).header(Header.WWW_AUTHENTICATE, "Basic");
    }
  }

  private boolean anyUsernameProvided(Context ctx) {
    if (!ctx.basicAuthCredentialsExist()) {
      return false;
    }

    return !ctx.basicAuthCredentials().getUsername().isEmpty();
  }
}
