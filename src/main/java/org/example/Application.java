package org.example;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.vue.VueComponent;
import org.example.dao.UserRepository;
import org.example.dao.UserNotFoundException;

public class Application {
  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
        config.enableWebjars();
        config.accessManager(new SessionManager());
    });

    app.get("/", ctx -> ctx.redirect("/users"), AppRole.ANYONE);
    app.get("/users", new VueComponent("user-overview"), AppRole.ANYONE);
    app.get("/users/{user-id}", new VueComponent("user-profile"), AppRole.LOGGED_IN);
    app.error(404, "html", new VueComponent("not-found"));

    app.get("/api/users", UserRepository::getAll, AppRole.ANYONE);
    app.get("/api/users/{user-id}", UserRepository::getOne, AppRole.LOGGED_IN);
    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404).result(e.getMessage()));

    app.start(7070);
  }
}
