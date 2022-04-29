package org.example.controllers;

import io.javalin.http.Context;
import org.example.repository.PostRepository;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class HomeController {
  private final PostRepository postRepository;

  public HomeController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public void getUserListing(Context ctx) {
    ctx.render("home.peb", model("posts", this.postRepository.getPosts()));
  }
}
