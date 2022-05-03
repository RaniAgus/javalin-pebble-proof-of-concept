package org.example.controller;

import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import org.example.data.User;
import org.example.repository.UserNotFoundException;
import org.example.repository.UserRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class SessionController implements AccessManager {
  private final UserRepository users;

  public SessionController(UserRepository users) {
    this.users = users;
  }

  @Override
  public void manage(@NotNull Handler handler,
                     @NotNull Context ctx,
                     @NotNull Set<RouteRole> roles) throws Exception {
    Long userId = ctx.sessionAttribute("userId");
    if (roles.isEmpty() || roles.contains(users.getById(userId).getRole())) {
      handler.handle(ctx);
    } else {
      ctx.status(HttpCode.NOT_FOUND);
    }
  }

  public void validateUserNotLoggedIn(Context ctx) {
    if (ctx.sessionAttribute("userId") != null) {
      ctx.redirect("/");
    }
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
      ctx.sessionAttribute("userId", user.getId());

      ctx.redirect("/" + ctx.formParam("origin"));
    } catch (UserNotFoundException e) {
      ctx.status(HttpCode.UNAUTHORIZED).redirect("/login?error=true");
    }
  }

  public void logout(Context ctx) {
    ctx.consumeSessionAttribute("userId");
    ctx.redirect("/");
  }
}
