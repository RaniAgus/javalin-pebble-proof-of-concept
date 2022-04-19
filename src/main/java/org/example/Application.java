package org.example;

import io.javalin.Javalin;
import org.example.auth.AppRole;
import org.example.controllers.RenderController;
import org.example.dao.UserNotFoundException;
import org.example.dao.UserRepository;

public class Application {
  private static final UserRepository userRepository = new UserRepository();
  private static final RenderController renderController = new RenderController(userRepository);

  public static void main(String[] args) {
    Javalin app = Javalin.create(new ApplicationConfig()).start(7070);

    app.get("/", ctx -> ctx.redirect("/users"), AppRole.ANYONE);
    app.get("/users", renderController::getAll, AppRole.ANYONE);
    app.get("/users/{user-id}", renderController::getOne, AppRole.LOGGED_IN);

    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404));
    app.error(404, "html", ctx -> ctx.render("not-found.html"));
  }
}
