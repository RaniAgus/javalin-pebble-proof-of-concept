package org.example;

import io.javalin.Javalin;
import io.javalin.http.HttpCode;
import org.example.controller.HomeController;
import org.example.controller.ProfileController;
import org.example.controller.SessionController;
import org.example.data.Role;
import org.example.repository.UserNotFoundException;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.plugin.rendering.template.TemplateUtil.model;
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

    Javalin.create(new ApplicationConfig(SESSION_CONTROLLER))
        .routes(() -> {
          get("", ctx -> ctx.redirect("/home"));
          get("home", HOME_CONTROLLER::getUserListing);
          path("login", () -> {
            before(SESSION_CONTROLLER::validateUserNotLoggedIn);
            get(SESSION_CONTROLLER::getLogin);
            post(SESSION_CONTROLLER::login);
          });
          get("logout", SESSION_CONTROLLER::logout);
          path("profiles", () -> {
            path("me", () -> {
              before(PROFILE_CONTROLLER::validateUserLoggedIn);
              get(PROFILE_CONTROLLER::getUserProfileBySession);
              post(PROFILE_CONTROLLER::editLoggedUserProfile);
              get("edit", PROFILE_CONTROLLER::getEditUserProfileFormAsUser);
            });
            path("{userId}", () -> {
              get(PROFILE_CONTROLLER::getUserProfileByPath);
              post(PROFILE_CONTROLLER::editPathUserProfile, Role.ADMIN);
              get("edit", PROFILE_CONTROLLER::getEditUserProfileFormAsAdmin, Role.ADMIN);
            });
          });
          after(ctx -> {
            if (getEntityManager().isOpen())
              closeEntityManager();
          });
        })
        .exception(UserNotFoundException.class, (e, ctx) -> ctx.status(HttpCode.NOT_FOUND))
        .error(404, "html", ctx -> ctx.render(
            "not-found.peb", model("userId", ctx.sessionAttribute("userId"))
        ))
        .start(7070);

  }
}
