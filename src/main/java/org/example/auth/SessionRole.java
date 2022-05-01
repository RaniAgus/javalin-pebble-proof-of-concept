package org.example.auth;

import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;

public enum SessionRole implements RouteRole {
  ANYONE {
    @Override
    boolean verify(Context ctx) {
      return true;
    }
  },
  LOGGED_IN {
    @Override
    boolean verify(Context ctx) {
      Long userId = ctx.sessionAttribute("userId");
      if (userId == null) {
        ctx.status(403).redirect(
            "/login?origin=" + ctx.path()
                .substring(1)
                .replace("/", "%2F")
        );
        return false;
      }
      return true;
    }
  },
  NOT_LOGGED_IN {
    @Override
    boolean verify(Context ctx) {
      if (ctx.sessionAttribute("userId") != null) {
        ctx.redirect("/");
        return false;
      }
      return true;
    }
  };

  abstract boolean verify(Context ctx);
}
