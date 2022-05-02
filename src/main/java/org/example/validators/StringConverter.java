package org.example.validators;

import kotlin.jvm.functions.Function1;

public class StringConverter implements Function1<String, String> {
  @Override
  public String invoke(String s) {
    // Javalin ya incluye un converter por defecto, pero lo sobreescribo para
    // tratar de la misma forma a un string vacío y a null
    return !s.isEmpty() ? s : null;
  }
}
