package org.example.controllers;

import io.javalin.http.Context;
import org.example.service.UserService;

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
}
