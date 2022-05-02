package org.example;

import io.javalin.Javalin;
import org.example.auth.SessionRole;
import org.example.controller.HomeController;
import org.example.controller.ProfileController;
import org.example.controller.SessionController;
import org.example.repository.UserNotFoundException;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;

import static io.javalin.apibuilder.ApiBuilder.*;
import static org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers.closeEntityManager;
import static org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers.getEntityManager;

public class Application {
  // Repository layer
  private static final PostRepository POST_REPOSITORY = new PostRepository();
  private static final UserRepository USER_REPOSITORY = new UserRepository();

  // Controller layer
  private static final HomeController HOME_CONTROLLER = new HomeController(POST_REPOSITORY);
  private static final SessionController SESSION_CONTROLLER = new SessionController(USER_REPOSITORY);
  private static final ProfileController PROFILE_CONTROLLER = new ProfileController(USER_REPOSITORY);

  public static void main(String[] args) {
    new Bootstrap().run();

    Javalin app = Javalin.create(new ApplicationConfig()).start(7070);

    app.get("/", ctx -> ctx.redirect("/home"));
    app.get("/home", HOME_CONTROLLER::getUserListing);
    app.get("/logout", SESSION_CONTROLLER::logout);
    app.routes(() -> {
      path("login", () -> {
        get(SESSION_CONTROLLER::getLogin, SessionRole.NOT_LOGGED_IN);
        post(SESSION_CONTROLLER::login, SessionRole.NOT_LOGGED_IN);
      });
      path("profiles", () -> {
        path("me", () -> {
          get(PROFILE_CONTROLLER::getUserProfileBySession, SessionRole.LOGGED_IN);
          post(PROFILE_CONTROLLER::editUserProfile, SessionRole.LOGGED_IN);
          get("edit", PROFILE_CONTROLLER::getEditUserProfileForm, SessionRole.LOGGED_IN);
        });
        get("{id}", PROFILE_CONTROLLER::getUserProfileByPath);
      });
    });
    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404));
    app.error(401, "html", ctx -> ctx.render("unauthorized.peb"));
    app.error(404, "html", ctx -> ctx.render("not-found.peb"));
    app.after(ctx -> {
      if (getEntityManager().isOpen()) {
        closeEntityManager();
      }
    });
  }
}
