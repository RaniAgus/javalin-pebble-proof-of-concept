package org.example.validators;

import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.ValidationException;

import static java.util.Collections.*;

public class ValidationUtil {
  public static ValidationException nullcheckFailedException(String param) {
    return new ValidationException(singletonMap(param, singletonList(
        new ValidationError<>("NULLCHECK_FAILED", emptyMap(), null)
    )));
  }
}
