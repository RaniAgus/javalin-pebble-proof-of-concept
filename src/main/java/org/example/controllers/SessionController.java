package org.example.controllers;

import io.javalin.http.Context;
import org.example.data.User;
import org.example.repository.UserNotFoundException;
import org.example.service.UserService;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class SessionController extends BaseController {
  private final UserService userService;

  public SessionController(UserService userService) {
    this.userService = userService;
  }

  public void getLogin(Context ctx) {
    render(ctx, "login.peb", model(
        "origin", ctx.queryParam("origin"),
        "error", ctx.queryParamAsClass("error", Boolean.class).allowNullable().get()
    ));
  }

  public void login(Context ctx) {
    try {
      String email = ctx.formParamAsClass("email", String.class).get();
      User user = this.userService.getUserByEmail(email);

      // TODO: Check user password

      ctx.cookie("userId", user.getId().toString());
      ctx.redirect("/" + ctx.formParam("origin"));
    } catch (UserNotFoundException e) {
      ctx.redirect("/login?error=true");
    }
  }

  public void logout(Context ctx) {
    ctx.removeCookie("userId");
    ctx.redirect("/");
  }
}
