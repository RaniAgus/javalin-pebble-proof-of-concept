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
    if (roles.contains(SessionRole.ANYONE)) {
      handler.handle(ctx);
    } else if (roles.contains(SessionRole.LOGGED_IN)) {
      verifyLoggedIn(handler, ctx);
    } else if (roles.contains(SessionRole.NOT_LOGGED_IN)) {
      verifyNotLoggedIn(handler, ctx);
    } else {
      ctx.status(401);
    }
  }

  private void verifyLoggedIn(Handler handler, Context ctx) throws Exception {
    Long userId = JWT.verify(ctx.cookie("session"));
    if (userId == null) {
      ctx.removeCookie("session");
      ctx.redirect("/login?origin=" + ctx.path().substring(1).replace("/", "%2F"));
    } else if (!ctx.pathParam("id").equals(userId.toString())) {
      ctx.status(401);
    } else {
      handler.handle(ctx);
    }
  }

  private void verifyNotLoggedIn(Handler handler, Context ctx) throws Exception {
    if (JWT.verify(ctx.cookie("session")) != null) {
      ctx.redirect("/" + ctx.queryParam("origin"));
    } else {
      ctx.removeCookie("session");
      handler.handle(ctx);
    }
  }
}
