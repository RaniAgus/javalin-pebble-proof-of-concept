package org.example.controller;

import io.javalin.core.validation.ValidationException;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.javalin.http.UploadedFile;
import org.example.form.EditUserProfileForm;
import org.example.data.User;
import org.example.repository.UserNotFoundException;
import org.example.repository.UserRepository;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.time.LocalDate;

import static io.javalin.core.util.FileUtil.streamToFile;
import static io.javalin.plugin.rendering.template.TemplateUtil.model;
import static java.time.LocalDate.now;

public class ProfileController implements WithGlobalEntityManager, TransactionalOps {
  private final UserRepository users;

  public ProfileController(UserRepository userRepository) {
    this.users = userRepository;
  }

  public void validateUserLoggedIn(Context ctx) {
    Long userId = ctx.sessionAttribute("userId");
    if (userId == null) {
      ctx.redirect("/login?origin=" + ctx.path().substring(1).replace("/", "%2F"));
    }
  }

  private void getUserProfile(Context ctx, Long id) {
    Long userId = ctx.sessionAttribute("userId");
    ctx.render("profile.peb", model(
        "userId", ctx.sessionAttribute("userId"),
        "user", this.users.getById(id),
        "isAdmin", userId != null && this.users.getById(userId).isAdmin()
    ));
  }

  public void getUserProfileByPath(Context ctx) {
    Long userId = ctx.pathParamAsClass("userId", Long.class)
        .getOrThrow(e -> new UserNotFoundException());
    this.getUserProfile(ctx, userId);
  }

  public void getUserProfileBySession(Context ctx) {
    this.getUserProfile(ctx, ctx.sessionAttribute("userId"));
  }

  public void getEditUserProfileFormAsUser(Context ctx) {
    Long userId = ctx.sessionAttribute("userId");
    getEditUserProfileForm(ctx, userId, false);
  }

  public void getEditUserProfileFormAsAdmin(Context ctx) {
    Long userId = ctx.pathParamAsClass("userId", Long.class)
        .getOrThrow(e -> new UserNotFoundException());
    getEditUserProfileForm(ctx, userId, true);
  }

  private void getEditUserProfileForm(Context ctx, Long userId, boolean isAdmin) {
    ctx.render("profile-edit.peb", model(
        "userId", userId,
        "user", this.users.getById(userId),
        "now", now(),
        "error", ctx.queryParam("error"),
        "isAdmin", isAdmin
    ));
  }

  public void editLoggedUserProfile(Context ctx) {
    editUserProfile(ctx, ctx.sessionAttribute("userId"), "me");
  }

  public void editPathUserProfile(Context ctx) {
    Long userId = ctx.pathParamAsClass("userId", Long.class)
        .getOrThrow(e -> new UserNotFoundException());
    editUserProfile(ctx, userId, userId.toString());
  }

  private void editUserProfile(Context ctx, Long userId, String userPath) {
    EditUserProfileForm form = new EditUserProfileForm()
        .setId(ctx.formParamAsClass("id", Long.class), userId)
        .setFirstName(ctx.formParamAsClass("firstName", String.class))
        .setLastName(ctx.formParamAsClass("lastName", String.class))
        .setBirthday(ctx.formParamAsClass("birthday", LocalDate.class))
        .setGender(ctx.formParamAsClass("gender", String.class))
        .setEmail(ctx.formParamAsClass("email", String.class))
        .setPhoto(ctx.uploadedFile("photo"));

    try {
      User user = users.getById(form.getId());
      form.fillUser(user);
      UploadedFile photo = form.getPhoto();

      entityManager().getTransaction().begin();
      this.users.update(user);
      streamToFile(photo.getContent(), "static/images/" + user.getId() + ".jpg");
      entityManager().getTransaction().commit();

      ctx.redirect("/profiles/" + userPath);
    } catch (ValidationException e) {
      ctx.status(HttpCode.BAD_REQUEST).json(form.getErrors());
    } catch (Exception e) {
      ctx.status(HttpCode.INTERNAL_SERVER_ERROR).redirect("/profiles/" + userPath + "/edit?error=true");
    }
  }
}
