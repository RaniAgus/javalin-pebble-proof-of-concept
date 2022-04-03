package org.example;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinPebble;
import org.example.auth.AppRole;
import org.example.auth.AuthManager;
import org.example.controllers.RenderController;
import org.example.dao.UserNotFoundException;

public class PebbleApplication {
  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
      config.addStaticFiles("public", Location.CLASSPATH);
      config.accessManager(new AuthManager());
      JavalinRenderer.register(JavalinPebble.INSTANCE, ".html");
      JavalinPebble.configure(pebbleEngine());
    });

    app.get("/", ctx -> ctx.redirect("/users"), AppRole.ANYONE);
    app.get("/users", RenderController::getAll, AppRole.ANYONE);
    app.get("/users/{user-id}", RenderController::getOne, AppRole.LOGGED_IN);
    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404));
    app.error(404, "html", ctx -> ctx.render("not-found.html"));

    app.start(7070);
  }

  private static PebbleEngine pebbleEngine() {
    return new PebbleEngine.Builder()
        .loader(classpathLoader())
        .strictVariables(true)
        .build();
  }

  private static ClasspathLoader classpathLoader() {
    ClasspathLoader classpathLoader = new ClasspathLoader();
    classpathLoader.setPrefix("templates");
    classpathLoader.setCharset("UTF-8");

    return classpathLoader;
  }
}
