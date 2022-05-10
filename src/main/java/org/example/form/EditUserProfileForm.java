package org.example.form;

import io.javalin.core.validation.*;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.apache.commons.validator.routines.EmailValidator;
import org.example.data.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static io.javalin.core.validation.JavalinValidation.collectErrors;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.*;

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
        .check(it -> it.equals(userId), "Not a valid user ID");
    firstName = ctx.formParamAsClass("firstName", String.class)
        .check(it -> !it.isEmpty(), "First name is required");
    lastName = ctx.formParamAsClass("lastName", String.class)
        .check(it -> !it.isEmpty(), "Last name is required");
    birthday = ctx.formParamAsClass("birthday", LocalDate.class)
        .allowNullable()
        .check(
            it -> it == null || it.isBefore(now().plusDays(1)),
            "Birthday must be today or before"
        );
    gender = ctx.formParamAsClass("gender", String.class)
        .check(
            it -> asList("Female", "Male", "Other").contains(it),
            "Gender must be one of: Female, Male, Other"
        );
    email = ctx.formParamAsClass("email", String.class)
        .check(
            it -> EmailValidator.getInstance().isValid(it),
            "Email address is not in a valid format"
        );
    photo = ctx.uploadedFile("photo");
  }

  public Map<String, List<ValidationError<?>>> getErrors() {
    Map<String, List<ValidationError<?>>> errors =
        collectErrors(id, firstName, lastName, birthday, gender, email);

    if (photo != null && !"image/jpeg".equals(photo.getContentType())) {
      errors.put("photo", singletonList(new ValidationError<>(
          "Photo must be in jpeg format", emptyMap(), photo.getContentType()
      )));
    }

    return errors;
  }

  public Long getId() {
    return id.get();
  }

  public void fillUser(User user) {
    user.setFirstName(firstName.get());
    user.setLastName(lastName.get());
    user.setBirthday(birthday.get());
    user.setGender(gender.get());
    user.setEmail(email.get());
  }

  public UploadedFile getPhoto() {
    return photo != null && photo.getSize() > 0 ? photo : null;
  }

}
