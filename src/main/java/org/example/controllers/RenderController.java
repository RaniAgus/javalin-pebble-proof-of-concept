package org.example.controllers;

import io.javalin.http.Context;
import org.example.dao.UserRepository;
import org.example.model.User;

import java.util.List;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public abstract class RenderController {
  public static void getAll(Context ctx) {
    List<User> users = UserRepository.findAll();
    ctx.render("users/user-overview.html", model("users", users));
  }

  public static void getOne(Context ctx) {
    User user = UserRepository.findFirstById(ctx.pathParam("user-id"));
    ctx.render("users/user-profile.html", model("user", user));
  }
}
