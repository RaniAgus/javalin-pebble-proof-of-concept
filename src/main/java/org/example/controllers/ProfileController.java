package org.example.controllers;

import io.javalin.http.Context;
import org.example.data.User;
import org.example.service.UserService;

import java.time.LocalDate;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class ProfileController {
  private final UserService userService;

  public ProfileController(UserService userService) {
    this.userService = userService;
  }

  public void getUserProfile(Context ctx) {
    String id = ctx.pathParam("id");
    ctx.render("profile.peb", model(
        "user", this.userService.getUser(id)
    ));
  }

  public void getEditUserProfileForm(Context ctx) {
    String id = ctx.pathParam("id");
    ctx.render("profile-edit.peb", model(
        "user", this.userService.getUser(id)
    ));
  }

  public void editUserProfile(Context ctx) {
    User user = new User(
        ctx.formParamAsClass("id", String.class).get(),
        ctx.formParamAsClass("firstName", String.class).get(),
        ctx.formParamAsClass("lastName", String.class).get(),
        ctx.formParamAsClass("birthday", LocalDate.class).get(),
        ctx.formParamAsClass("gender", String.class).get(),
        ctx.formParamAsClass("email", String.class).get()
    );
    this.userService.putUser(user);

    ctx.redirect("/profiles/" + user.getId());
  }

}
