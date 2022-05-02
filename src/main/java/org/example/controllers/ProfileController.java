package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.data.EditUserProfileForm;
import org.example.data.User;
import org.example.repository.UserRepository;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import static io.javalin.core.util.FileUtil.streamToFile;
import static io.javalin.plugin.rendering.template.TemplateUtil.model;
import static java.time.LocalDate.now;

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
    EditUserProfileForm form = new EditUserProfileForm(ctx);
    if (!form.isValid()) {
      ctx.status(400).redirect("/profiles/me/edit?error=true");
      return;
    }

    try {
      User user = form.updateUser(users.getById(form.getId()));
      UploadedFile photo = form.getPhoto();

      entityManager().getTransaction().begin();
      this.users.update(user);
      if (photo != null) {
        streamToFile(photo.getContent(), "static/images/" + user.getId() + ".jpg");
      }
      entityManager().getTransaction().commit();

      ctx.redirect("/profiles/me");
    } catch (Exception e) {
      ctx.status(500).redirect("/profiles/me/edit?error=true");
    }
  }
}
