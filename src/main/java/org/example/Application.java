package org.example;

import io.javalin.Javalin;
import org.example.controllers.HomeController;
import org.example.controllers.ProfileController;
import org.example.repository.UserNotFoundException;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.service.PostService;
import org.example.service.UserService;

public class Application {
  // Repository layer
  private static final PostRepository POST_REPOSITORY = new PostRepository();
  private static final UserRepository USER_REPOSITORY = new UserRepository();

  // Service layer
  private static final PostService POST_SERVICE = new PostService(POST_REPOSITORY);
  private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY);

  // Controller layer
  private static final HomeController HOME_CONTROLLER = new HomeController(POST_SERVICE);
  private static final ProfileController PROFILE_CONTROLLER = new ProfileController(USER_SERVICE);

  public static void main(String[] args) {
    new Bootstrap().run();

    Javalin app = Javalin.create(new ApplicationConfig()).start(7070);

    app.get("/", ctx -> ctx.redirect("/home"));
    app.get("/home", HOME_CONTROLLER::getUserListing);
    app.get("/profiles/{id}", PROFILE_CONTROLLER::getUserProfile);
    app.get("/profiles/{id}/edit", PROFILE_CONTROLLER::getEditUserProfileForm);
    app.post("/profiles/{id}", PROFILE_CONTROLLER::editUserProfile);

    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404));
    app.error(404, "html", ctx -> ctx.render("not-found.peb"));
  }
}
