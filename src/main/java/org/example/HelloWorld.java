package org.example;

import io.javalin.Javalin;

public class HelloWorld {
  public static void main(String[] args) {
    Javalin app = Javalin.create().start(7070);
    app.get("/", ctx -> ctx.result("Hello World"));
  }
}
