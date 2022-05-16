package org.example.form;

import io.javalin.core.validation.*;
import io.javalin.http.UploadedFile;
import org.apache.commons.validator.routines.EmailValidator;
import org.example.data.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.core.validation.JavalinValidation.collectErrors;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.*;

public class EditUserProfileForm {
  private Validator<Long> id;
  private Validator<String> firstName;
  private Validator<String> lastName;
  private NullableValidator<LocalDate> birthday;
  private Validator<String> gender;
  private Validator<String> email;
  private UploadedFile photo;
  Map<String, List<ValidationError<?>>> photoErrors = new HashMap<>();

  public EditUserProfileForm setId(Validator<Long> id, Long userId) {
    this.id = id
        .check(it -> it.equals(userId), "Not a valid user ID");
    return this;
  }

  public EditUserProfileForm setFirstName(Validator<String> firstName) {
    this.firstName = firstName
        .check(it -> !it.isEmpty(), "First name is required");
    return this;
  }

  public EditUserProfileForm setLastName(Validator<String> lastName) {
    this.lastName = lastName
        .check(it -> !it.isEmpty(), "Last name is required");
    return this;
  }

  public EditUserProfileForm setBirthday(Validator<LocalDate> birthday) {
    this.birthday = birthday
        .allowNullable()
        .check(it -> it == null || it.isBefore(now().plusDays(1)),
            "Birthday must be today or before");
    return this;
  }

  public EditUserProfileForm setGender(Validator<String> gender) {
    this.gender = gender
        .check(it -> asList("Female", "Male", "Other").contains(it),
            "Gender must be one of: Female, Male, Other");
    return this;
  }

  public EditUserProfileForm setEmail(Validator<String> email) {
    this.email = email
        .check(it -> EmailValidator.getInstance().isValid(it),
            "Email address is not in a valid format");
    return this;
  }

  public EditUserProfileForm setPhoto(UploadedFile photo) {
    if (photo == null) {
      photoErrors.put("photo", validationError("NULLCHECK_FAILED", null));
    } else if (!"image/jpeg".equals(photo.getContentType())) {
      photoErrors.put("photo", validationError(
          "Photo must be in jpeg format", photo.getContentType()));
    }
    this.photo = photo;
    return this;
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
    if (!photoErrors.isEmpty()) {
      throw new ValidationException(emptyMap());
    }
    return photo;
  }

  public Map<String, List<ValidationError<?>>> getErrors() {
    Map<String, List<ValidationError<?>>> errors =
        collectErrors(id, firstName, lastName, birthday, gender, email);
    errors.putAll(photoErrors);

    return errors;
  }

  private static List<ValidationError<?>> validationError(String message, Object value) {
    return singletonList(new ValidationError<>(message, emptyMap(), value));
  }

}
