package org.example;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.plugin.rendering.vue.VueComponent;

public class HelloWorld {
  public static void main(String[] args) {
    Javalin app = Javalin.create(JavalinConfig::enableWebjars);

    app.get("/", new VueComponent("hello-world"));

    app.start(7070);
  }
}
