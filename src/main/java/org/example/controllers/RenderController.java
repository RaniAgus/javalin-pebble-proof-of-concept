package org.example.controllers;

import io.javalin.http.Context;
import org.example.dao.UserRepository;
import org.example.model.User;

import java.util.List;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class RenderController {
  private final UserRepository users;

  public RenderController(UserRepository users) {
    this.users = users;
  }

  public void getAll(Context ctx) {
    List<User> users = this.users.findAll();
    ctx.render("users/user-overview.peb", model("users", users));
  }

  public void getOne(Context ctx) {
    User user = this.users.findFirstById(ctx.pathParam("user-id"));
    ctx.render("users/user-profile.peb", model("user", user));
  }
}
