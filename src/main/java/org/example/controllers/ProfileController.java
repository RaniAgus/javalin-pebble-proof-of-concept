package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.data.User;
import org.example.service.UserService;

import java.time.LocalDate;

import static io.javalin.plugin.rendering.template.TemplateUtil.model;

public class ProfileController extends BaseController {
  private final UserService userService;

  public ProfileController(UserService userService) {
    this.userService = userService;
  }

  public void getUserProfile(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    render(ctx, "profile.peb", model("user", this.userService.getUser(id)));
  }

  public void getEditUserProfileForm(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();
    render(ctx, "profile-edit.peb", model("user", this.userService.getUser(id)));
  }

  public void editUserProfile(Context ctx) {
    UploadedFile photo = getUploadedFile(ctx, "photo");
    User user = new User(
        ctx.formParamAsClass("firstName", String.class).get(),
        ctx.formParamAsClass("lastName", String.class).get(),
        ctx.formParamAsClass("birthday", LocalDate.class).allowNullable().get(),
        ctx.formParamAsClass("gender", String.class).get(),
        ctx.formParamAsClass("email", String.class).get()
    );
    user.setId(ctx.formParamAsClass("id", Long.class).get());

    this.userService.editUserProfile(user, photo);
    ctx.redirect("/profiles/" + user.getId());
  }
}
