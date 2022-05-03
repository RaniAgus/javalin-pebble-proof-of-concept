package org.example;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import io.javalin.core.JavalinConfig;
import io.javalin.core.security.AccessManager;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinPebble;
import kotlin.jvm.functions.Function1;

import java.time.LocalDate;
import java.util.function.Consumer;

public class ApplicationConfig implements Consumer<JavalinConfig> {
  private final AccessManager accessManager;

  public ApplicationConfig(AccessManager accessManager) {
    this.accessManager = accessManager;
  }

  @Override
  public void accept(JavalinConfig javalinConfig) {
    javalinConfig.addStaticFiles("static", Location.EXTERNAL);
    javalinConfig.accessManager(this.accessManager);
    JavalinRenderer.register(JavalinPebble.INSTANCE, ".peb");
    JavalinPebble.configure(configureEngine());
    JavalinValidation.register(LocalDate.class, localDateConverter());
  }

  private PebbleEngine configureEngine() {
    ClasspathLoader classpathLoader = new ClasspathLoader();
    classpathLoader.setPrefix("templates");
    classpathLoader.setCharset("UTF-8");

    return new PebbleEngine.Builder()
        .loader(classpathLoader)
        .build();
  }

  private Function1<String, LocalDate> localDateConverter() {
    return s -> !s.isEmpty() ? LocalDate.parse(s) : null;
  }
}
