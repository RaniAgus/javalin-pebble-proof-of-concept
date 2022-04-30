package org.example;

import io.javalin.Javalin;
import org.example.auth.SessionRole;
import org.example.controllers.HomeController;
import org.example.controllers.ProfileController;
import org.example.controllers.SessionController;
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
  private static final SessionController SESSION_CONTROLLER = new SessionController(USER_SERVICE);
  private static final ProfileController PROFILE_CONTROLLER = new ProfileController(USER_SERVICE);

  public static void main(String[] args) {
    new Bootstrap().run();

    Javalin app = Javalin.create(new ApplicationConfig()).start(7070);

    app.get("/", ctx -> ctx.redirect("/home"), SessionRole.ANYONE);
    app.get("/home", HOME_CONTROLLER::getUserListing, SessionRole.ANYONE);
    app.get("/login", SESSION_CONTROLLER::getLogin, SessionRole.NOT_LOGGED_IN);
    app.post("/login", SESSION_CONTROLLER::login, SessionRole.NOT_LOGGED_IN);
    app.get("/logout", SESSION_CONTROLLER::logout, SessionRole.ANYONE);
    app.get("/profiles/{id}", PROFILE_CONTROLLER::getUserProfile, SessionRole.ANYONE);
    app.get("/profiles/{id}/edit", PROFILE_CONTROLLER::getEditUserProfileForm, SessionRole.LOGGED_IN);
    app.post("/profiles/{id}", PROFILE_CONTROLLER::editUserProfile, SessionRole.LOGGED_IN);

    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404));
    app.error(401, "html", ctx -> ctx.render("unauthorized.peb"));
    app.error(404, "html", ctx -> ctx.render("not-found.peb"));
  }
}
