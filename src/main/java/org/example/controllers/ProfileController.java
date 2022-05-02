package org.example.controllers;

import io.javalin.core.validation.ValidationException;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.apache.commons.validator.routines.EmailValidator;
import org.example.data.User;
import org.example.repository.UserRepository;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.time.LocalDate;

import static io.javalin.core.util.FileUtil.streamToFile;
import static io.javalin.plugin.rendering.template.TemplateUtil.model;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;

public class ProfileController implements WithGlobalEntityManager, TransactionalOps {
  private final UserRepository users;

  public ProfileController(UserRepository userRepository) {
    this.users = userRepository;
  }

  private void getUserProfile(Context ctx, Long id) {
    ctx.render("profile.peb", model(
        "userId", ctx.sessionAttribute("userId"),
        "user", this.users.getById(id)
    ));
  }

  public void getUserProfileByPath(Context ctx) {
    this.getUserProfile(ctx, ctx.pathParamAsClass("id", Long.class).get());
  }

  public void getUserProfileBySession(Context ctx) {
    this.getUserProfile(ctx, ctx.sessionAttribute("userId"));
  }

  public void getEditUserProfileForm(Context ctx) {
    Long userId = ctx.sessionAttribute("userId");
    ctx.render("profile-edit.peb", model(
        "userId", ctx.sessionAttribute("userId"),
        "user", this.users.getById(userId),
        "now", now(),
        "error", ctx.queryParam("error")
    ));
  }

  public void editUserProfile(Context ctx) {
    Long userId = ctx.sessionAttribute("userId");
    try {
      User user = new User(
          ctx.formParamAsClass("firstName", String.class).get(),
          ctx.formParamAsClass("lastName", String.class).get(),
          ctx.formParamAsClass("birthday", LocalDate.class)
              .allowNullable()
              .check(
                  localDate -> localDate.isBefore(now().plusDays(1)),
                  "Birthday must be today or earlier."
              )
              .get(),
          ctx.formParamAsClass("gender", String.class)
              .check(
                  gender -> asList("Female", "Male", "Other").contains(gender),
                  "Gender must be one of: Female, Male, Other."
              )
              .get(),
          ctx.formParamAsClass("email", String.class)
              .check(
                  email -> EmailValidator.getInstance().isValid(email),
                  "Invalid email address."
              )
              .get()
      );
      user.setId(userId);
      UploadedFile photo = ctx.uploadedFile("photo");

      entityManager().getTransaction().begin();
      this.users.update(user);
      if (photo != null && photo.getSize() > 0) {
        streamToFile(photo.getContent(), "static/images/" + userId + ".jpg");
      }
      entityManager().getTransaction().commit();

      ctx.redirect("/profiles/me");

    } catch (ValidationException e) {
      ctx.status(400).redirect("/profiles/me/edit?error=true");
    } catch (Exception e) {
      ctx.status(500).redirect("/profiles/me/edit?error=true");
    }
  }
}
