package org.example;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import io.javalin.core.JavalinConfig;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinPebble;
import org.example.auth.SessionManager;
import org.example.validators.LocalDateConverter;
import org.example.validators.StringConverter;

import java.time.LocalDate;
import java.util.function.Consumer;

public class ApplicationConfig implements Consumer<JavalinConfig> {
  @Override
  public void accept(JavalinConfig javalinConfig) {
    javalinConfig.addStaticFiles("static", Location.EXTERNAL);
    javalinConfig.accessManager(new SessionManager());
    JavalinRenderer.register(JavalinPebble.INSTANCE, ".peb");
    JavalinPebble.configure(configureEngine());
    JavalinValidation.register(LocalDate.class, new LocalDateConverter());
    JavalinValidation.register(String.class, new StringConverter());
  }

  private PebbleEngine configureEngine() {
    ClasspathLoader classpathLoader = new ClasspathLoader();
    classpathLoader.setPrefix("templates");
    classpathLoader.setCharset("UTF-8");

    return new PebbleEngine.Builder()
        .loader(classpathLoader)
        .build();
  }

}
