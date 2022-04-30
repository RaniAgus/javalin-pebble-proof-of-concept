package org.example.controllers;

import io.javalin.http.Context;
import org.example.auth.JWT;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public class BaseController implements WithGlobalEntityManager, TransactionalOps {
  protected Long getSession(Context ctx) {
    return JWT.verify(ctx.cookie("session"));
  }

  protected void setSession(Context ctx, Long id) {
    ctx.cookie("session", JWT.sign(id));
  }

  protected void unsetSession(Context ctx) {
    ctx.removeCookie("session");
  }
}

