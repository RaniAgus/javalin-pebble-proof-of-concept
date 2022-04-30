package org.example.auth;

import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public enum SessionRole implements RouteRole {
  NONE {
    @Override
    boolean verify(Handler handler, Context ctx) {
      return false;
    }
  },
  LOGGED_IN {
    @Override
    boolean verify(Handler handler, Context ctx) {
      Long userId = JWT.getSession(ctx);
      if (userId == null) {
        JWT.clearSession(ctx);
        ctx.redirect(
            "/login?origin=" + ctx.path()
                .substring(1)
                .replace("/", "%2F")
        );
        return false;
      }

      if (!ctx.pathParam("id").equals(userId.toString())) {
        ctx.status(401);
        return false;
      }

      return true;
    }
  },
  NOT_LOGGED_IN {
    @Override
    boolean verify(Handler handler, Context ctx) {
      if (JWT.getSession(ctx) != null) {
        ctx.redirect("/" + ctx.queryParam("origin"));
        return false;
      }

      JWT.clearSession(ctx);
      return true;
    }
  };

  abstract boolean verify(Handler handler, Context ctx) throws Exception;
}
