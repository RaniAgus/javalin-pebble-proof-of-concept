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
    SessionRole role = (SessionRole) roles.stream().findFirst().orElse(null);
    if (role != null && !role.verify(handler, ctx)) {
      return;
    }

    handler.handle(ctx);
  }
}
