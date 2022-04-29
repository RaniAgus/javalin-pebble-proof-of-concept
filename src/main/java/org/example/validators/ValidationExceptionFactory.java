package org.example.validators;

import io.javalin.core.validation.ValidationError;
import io.javalin.core.validation.ValidationException;

import static java.util.Collections.*;

public class ValidationExceptionFactory {
  public static ValidationException nullcheckFailed(String param) {
    return new ValidationException(singletonMap(param, singletonList(
        new ValidationError<>("NULLCHECK_FAILED", emptyMap(), null)
    )));
  }
}
