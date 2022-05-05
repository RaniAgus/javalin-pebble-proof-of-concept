package org.example.form;

import io.javalin.core.validation.*;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.apache.commons.validator.routines.EmailValidator;
import org.example.data.User;

import java.time.LocalDate;
import java.util.Set;

import static io.javalin.core.validation.JavalinValidation.collectErrors;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;

public class EditUserProfileForm {
  private final Validator<Long> id;
  private final Validator<String> firstName;
  private final Validator<String> lastName;
  private final NullableValidator<LocalDate> birthday;
  private final Validator<String> gender;
  private final Validator<String> email;
  private final UploadedFile photo;

  public EditUserProfileForm(Context ctx, Long userId) {
    id = ctx.formParamAsClass("id", Long.class)
        .check(id -> id.equals(userId), "");
    firstName = ctx.formParamAsClass("firstName", String.class)
        .check(firstName -> !firstName.isEmpty(), "");
    lastName = ctx.formParamAsClass("lastName", String.class)
        .check(lastName -> !lastName.isEmpty(), "");
    birthday = ctx.formParamAsClass("birthday", LocalDate.class)
        .allowNullable()
        .check(localDate -> localDate.isBefore(now().plusDays(1)), "");
    gender = ctx.formParamAsClass("gender", String.class)
        .check(gender -> asList("Female", "Male", "Other").contains(gender), "");
    email = ctx.formParamAsClass("email", String.class)
        .check(email -> EmailValidator.getInstance().isValid(email), "");
    photo = ctx.uploadedFile("photo");
  }

  public boolean isValid() {
    Set<String> errors = collectErrors(id, firstName, lastName, gender, email).keySet();
    errors.addAll(birthday.errors().keySet());
    if (photo != null && !"image/jpeg".equals(photo.getContentType())) {
      errors.add("photo");
    }

    return errors.isEmpty();
  }

  public Long getId() {
    return id.get();
  }

  public User updateUser(User user) {
    user.setFirstName(firstName.get());
    user.setLastName(lastName.get());
    user.setBirthday(birthday.get());
    user.setGender(gender.get());
    user.setEmail(email.get());

    return user;
  }

  public UploadedFile getPhoto() {
    return photo != null && photo.getSize() > 0 ? photo : null;
  }
}
