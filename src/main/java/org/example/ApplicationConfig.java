package org.example;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import io.javalin.core.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinPebble;
import org.example.auth.AuthManager;

import java.util.function.Consumer;

public class ApplicationConfig implements Consumer<JavalinConfig> {
  @Override
  public void accept(JavalinConfig javalinConfig) {
    javalinConfig.addStaticFiles("public", Location.CLASSPATH);
    javalinConfig.accessManager(new AuthManager());
    JavalinRenderer.register(JavalinPebble.INSTANCE, ".peb");
    JavalinPebble.configure(getEngine());
  }

  private PebbleEngine getEngine() {
    ClasspathLoader classpathLoader = new ClasspathLoader();
    classpathLoader.setPrefix("templates");
    classpathLoader.setCharset("UTF-8");

    return new PebbleEngine.Builder()
        .loader(classpathLoader)
        .strictVariables(true)
        .build();
  }
}
