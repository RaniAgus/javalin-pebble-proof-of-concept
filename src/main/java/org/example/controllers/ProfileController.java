package org.example.controllers;

import io.javalin.http.Context;
import org.example.data.User;
import org.example.service.UserService;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.time.LocalDate;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class ProfileController implements WithGlobalEntityManager, TransactionalOps {
  private final UserService userService;

  public ProfileController(UserService userService) {
    this.userService = userService;
  }

  public void getUserProfile(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    ctx.render("profile.peb", model(
        "user", this.userService.getUser(id)
    ));
  }

  public void getEditUserProfileForm(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    ctx.render("profile-edit.peb", model(
        "user", this.userService.getUser(id)
    ));
  }

  public void editUserProfile(Context ctx) {
    User user = new User(
        ctx.formParamAsClass("firstName", String.class).get(),
        ctx.formParamAsClass("lastName", String.class).get(),
        ctx.formParamAsClass("birthday", LocalDate.class).get(),
        ctx.formParamAsClass("gender", String.class).get(),
        ctx.formParamAsClass("email", String.class).get()
    );
    user.setId(ctx.formParamAsClass("id", Long.class).get());

    entityManager().getTransaction().begin();
    this.userService.putUser(user);
    entityManager().getTransaction().commit();

    ctx.redirect("/profiles/" + user.getId());
  }

}
