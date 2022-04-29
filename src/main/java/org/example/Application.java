package org.example;

import io.javalin.Javalin;
import org.example.controllers.HomeController;
import org.example.controllers.ProfileController;
import org.example.service.UserNotFoundException;
import org.example.service.PostService;
import org.example.service.UserService;

public class Application {
  private static final PostService postService = new PostService();
  private static final UserService userService = new UserService();
  private static final HomeController homeController = new HomeController(postService);
  private static final ProfileController profileController = new ProfileController(userService);

  public static void main(String[] args) {
    Javalin app = Javalin.create(new ApplicationConfig()).start(7070);

    app.get("/", ctx -> ctx.redirect("/home"));
    app.get("/home", homeController::getUserListing);
    app.get("/profiles/{id}", profileController::getUserProfile);
    app.get("/profiles/{id}/edit", profileController::getEditUserProfileForm);
    app.post("/profiles/{id}/edit", profileController::editUserProfile);

    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404));
    app.error(404, "html", ctx -> ctx.render("not-found.peb"));
  }
}
