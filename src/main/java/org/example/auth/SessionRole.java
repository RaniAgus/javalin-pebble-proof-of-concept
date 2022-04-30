package org.example.auth;

import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public enum SessionRole implements RouteRole {
  ANYONE {
    @Override
    void handle(Handler handler, Context ctx) throws Exception {
      handler.handle(ctx);
    }
  },
  LOGGED_IN {
    @Override
    void handle(Handler handler, Context ctx) throws Exception {
      String userId = ctx.cookie("userId");
      if (userId == null) {
        ctx.redirect("/login?origin=" + ctx.path().substring(1).replace("/", "%2F"));
      } else if (!userId.equals(ctx.pathParam("id"))) {
        ctx.status(401);
      }
      handler.handle(ctx);
    }
  },
  NOT_LOGGED_IN {
    @Override
    void handle(Handler handler, Context ctx) throws Exception {
      if (ctx.cookie("userId") != null) {
        ctx.redirect("/" + ctx.queryParam("origin"));
      }
      handler.handle(ctx);
    }
  };
  abstract void handle(Handler handler, Context ctx) throws Exception;
}
