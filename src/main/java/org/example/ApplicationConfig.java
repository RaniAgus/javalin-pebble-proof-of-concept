package org.example;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import io.javalin.core.JavalinConfig;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinPebble;

import java.time.LocalDate;
import java.util.function.Consumer;

import static java.time.format.DateTimeFormatter.ofPattern;

public class ApplicationConfig implements Consumer<JavalinConfig> {
  @Override
  public void accept(JavalinConfig javalinConfig) {
    javalinConfig.addStaticFiles("static", Location.CLASSPATH);
    JavalinRenderer.register(JavalinPebble.INSTANCE, ".peb");
    JavalinPebble.configure(getEngine());
    JavalinValidation.register(LocalDate.class, text -> LocalDate.parse(text, ofPattern("yyyy-MM-dd")));
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
