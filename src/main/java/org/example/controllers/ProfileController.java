package org.example.controllers;

import io.javalin.core.validation.ValidationException;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.data.User;
import org.example.repository.UserRepository;

import java.time.LocalDate;

import static io.javalin.core.util.FileUtil.streamToFile;
import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class ProfileController extends BaseController {
  private final UserRepository users;

  public ProfileController(UserRepository userRepository) {
    this.users = userRepository;
  }

  public void getUserProfile(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    ctx.render("profile.peb", model(
        "userId", getSession(ctx),
        "user", this.users.getById(id)
    ));
  }

  public void getEditUserProfileForm(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    ctx.render("profile-edit.peb", model(
        "userId", getSession(ctx),
        "user", this.users.getById(id),
        "now", LocalDate.now(),
        "error", ctx.queryParam("error")
    ));
  }

  public void editUserProfile(Context ctx) {
    try {
      User user = new User(
          ctx.formParamAsClass("firstName", String.class).get(),
          ctx.formParamAsClass("lastName", String.class).get(),
          ctx.formParamAsClass("birthday", LocalDate.class).allowNullable().get(),
          ctx.formParamAsClass("gender", String.class).get(),
          ctx.formParamAsClass("email", String.class).get()
      );
      user.setId(ctx.formParamAsClass("id", Long.class).get());
      UploadedFile photo = ctx.uploadedFile("photo");

      entityManager().getTransaction().begin();
      this.users.update(user);
      if (photo != null && photo.getSize() > 0) {
        streamToFile(photo.getContent(), "static/images/" + user.getId() + ".jpg");
      }
      entityManager().getTransaction().commit();

      ctx.redirect("/profiles/" + user.getId());

    } catch (ValidationException e) {
      ctx.redirect("/profiles/" + ctx.pathParam("id") + "/edit?error=true");
    }
  }
}
