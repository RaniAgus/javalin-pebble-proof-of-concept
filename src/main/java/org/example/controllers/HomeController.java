package org.example.controllers;

import io.javalin.http.Context;
import org.example.auth.JWT;
import org.example.repository.PostRepository;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class HomeController {
  private final PostRepository posts;

  public HomeController(PostRepository posts) {
    this.posts = posts;
  }

  public void getUserListing(Context ctx) {
    ctx.render("home.peb", model(
        "userId", JWT.getSession(ctx),
        "posts", this.posts.getAll()
    ));
  }
}
