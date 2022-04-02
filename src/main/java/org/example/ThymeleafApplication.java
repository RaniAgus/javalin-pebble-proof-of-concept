package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.example.auth.AppRole;
import org.example.auth.AuthManager;
import org.example.dao.UserNotFoundException;
import org.example.dao.UserRepository;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class ThymeleafApplication {
  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
      config.addStaticFiles("/public", Location.CLASSPATH);
      config.accessManager(new AuthManager());
      JavalinRenderer.register(JavalinThymeleaf.INSTANCE);
      JavalinThymeleaf.configure(templateEngine());
    });

    app.get("/", ctx -> ctx.redirect("/users"), AppRole.ANYONE);

    app.get("/users", ctx -> {
      ctx.render("users/user-overview.html", model("users", UserRepository.findAll()));
    }, AppRole.ANYONE);

    app.get("/users/{user-id}", ctx -> {
      ctx.render(
          "users/user-profile.html",
          model("user", UserRepository.findFirstById(ctx.pathParam("user-id")))
      );
    }, AppRole.LOGGED_IN);

    app.exception(UserNotFoundException.class, (e, ctx) -> ctx.status(404));
    app.error(404, "html", ctx -> ctx.render("not-found.html"));

    app.start(7070);
  }

  public static TemplateEngine templateEngine() {
    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());

    return templateEngine;
  }

  private static ITemplateResolver templateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setPrefix("/thymeleaf/");
    templateResolver.setSuffix(".html");
    templateResolver.setCharacterEncoding("UTF-8");

    return templateResolver;
  }
}
