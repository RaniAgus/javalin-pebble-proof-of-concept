package org.example.form;

import io.javalin.core.validation.*;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.apache.commons.validator.routines.EmailValidator;
import org.example.data.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class EditUserProfileForm {
  private final Validator<Long> id;
  private final Validator<String> firstName;
  private final Validator<String> lastName;
  private final NullableValidator<LocalDate> birthday;
  private final Validator<String> gender;
  private final Validator<String> email;
  private final UploadedFile photo;
  private final Map<String, List<String>> errors;

  public EditUserProfileForm(Context ctx, Long userId) {
    this.id = ctx.formParamAsClass("id", Long.class)
        .check(id -> id.equals(userId), "Invalid user ID");
    this.firstName = ctx.formParamAsClass("firstName", String.class);
    this.lastName = ctx.formParamAsClass("lastName", String.class);
    this.birthday = ctx.formParamAsClass("birthday", LocalDate.class)
        .allowNullable()
        .check(
            localDate -> localDate == null || localDate.isBefore(now().plusDays(1)),
            "Birthday must be today or earlier."
        );
    this.gender = ctx.formParamAsClass("gender", String.class)
        .check(
            gender -> asList("Female", "Male", "Other").contains(gender),
            "Gender must be one of: Female, Male, Other."
        );
    this.email = ctx.formParamAsClass("email", String.class)
        .check(
            email -> EmailValidator.getInstance().isValid(email),
            "Invalid email address."
        );
    this.photo = ctx.uploadedFile("photo");
    this.errors = collectErrors();
  }

  public boolean isValid() {
    return this.errors.isEmpty();
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

  private Map<String, List<String>> collectErrors() {
    Map<String, List<String>> errors = Stream.of(id, firstName, lastName, birthday, gender, email)
        .flatMap(v -> v.errors().entrySet().stream())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            m -> m.getValue().stream().map(ValidationError::getMessage).collect(Collectors.toList())
        ));

    UploadedFile photo = getPhoto();
    String contentType = photo != null ? photo.getContentType() : null;
    if (photo != null && (contentType == null || !contentType.equals("image/jpeg"))) {
      errors.put("photo", singletonList("Photo must be in jpeg format"));
    }

    return errors;
  }
}
