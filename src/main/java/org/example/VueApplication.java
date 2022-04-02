package org.example;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.vue.JavalinVue;
import io.javalin.plugin.rendering.vue.VueComponent;
import org.example.api.UsersController;
import org.example.auth.AppRole;
import org.example.auth.AuthManager;
import org.example.dao.UserNotFoundException;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public class VueApplication {
  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
        config.enableWebjars();
        config.accessManager(new AuthManager());
    });

    JavalinVue.stateFunction = ctx -> ctx.basicAuthCredentialsExist() ?
        singletonMap("currentUser", ctx.basicAuthCredentials().getUsername()): emptyMap();

    app.get("/", ctx -> ctx.redirect("/users"), AppRole.ANYONE);
    app.get("/users", new VueComponent("user-overview"), AppRole.ANYONE);
    app.get("/users/{user-id}", new VueComponent("user-profile"), AppRole.LOGGED_IN);
    app.error(404, "html", new VueComponent("not-found"));

    app.get("/api/users", UsersController::getAll, AppRole.ANYONE);
    app.get("/api/users/{user-id}", UsersController::getOne, AppRole.LOGGED_IN);
    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404).result(e.getMessage()));

    app.start(7070);
  }
}
