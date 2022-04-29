package org.example.controllers;

import io.javalin.http.Context;
import org.example.service.PostService;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class HomeController extends BaseRepository {
  private final PostService postService;

  public HomeController(PostService postService) {
    this.postService = postService;
  }

  public void getUserListing(Context ctx) {
    ctx.render("home.peb", model("posts", this.postService.getPosts()));
  }
}
