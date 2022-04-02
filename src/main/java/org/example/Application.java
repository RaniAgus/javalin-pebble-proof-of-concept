package org.example;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.rendering.vue.VueComponent;
import org.example.dao.UserRepository;
import org.example.dao.UserNotFoundException;

public class Application {
  public static void main(String[] args) {
    Javalin app = Javalin.create(JavalinConfig::enableWebjars);

    app.get("/users", new VueComponent("user-overview"));
    app.get("/users/{user-id}", new VueComponent("user-profile"));

    app.get("/api/users", UserRepository::getAll);
    app.get("/api/users/{user-id}", UserRepository::getOne);
    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404).result(e.getMessage()));

    app.start(7070);
  }
}
