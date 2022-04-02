package org.example;

import io.javalin.Javalin;

public class HelloWorld {
  public static void main(String[] args) {
    Javalin app = Javalin.create();

    app.get("/", ctx -> ctx.result("Hello World"));

    app.get("/hello/{name}", ctx -> {
      ctx.result("Hello " + ctx.pathParam("name").toUpperCase());
    });

    app.get("/path/*", ctx -> {
      ctx.result("You are here because " + ctx.path() + " matches " + ctx.matchedPath());
    });

    app.start(7070);
  }
}
