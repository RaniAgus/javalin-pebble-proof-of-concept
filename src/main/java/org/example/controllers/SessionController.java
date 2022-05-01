package org.example.controllers;

import io.javalin.http.Context;
import org.example.auth.SessionToken;
import org.example.data.User;
import org.example.repository.UserNotFoundException;
import org.example.repository.UserRepository;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class SessionController {
  private final UserRepository users;

  public SessionController(UserRepository users) {
    this.users = users;
  }

  public void getLogin(Context ctx) {
    ctx.render("login.peb", model(
        "origin", ctx.queryParam("origin"),
        "error", ctx.queryParam("error")
    ));
  }

  public void login(Context ctx) {
    try {
      String email = ctx.formParamAsClass("email", String.class).get();
      User user = this.users.getByEmail(email);

      // TODO: Check user password
      SessionToken.set(ctx, user.getId());

      ctx.redirect("/" + ctx.formParam("origin"));
    } catch (UserNotFoundException e) {
      ctx.redirect("/login?error=true");
    }
  }

  public void logout(Context ctx) {
    SessionToken.clear(ctx);
    ctx.redirect("/");
  }
}
