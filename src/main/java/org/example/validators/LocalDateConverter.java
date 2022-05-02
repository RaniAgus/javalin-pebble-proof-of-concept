package org.example.validators;

import kotlin.jvm.functions.Function1;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

public class LocalDateConverter implements Function1<String, LocalDate> {
  @Override
  public LocalDate invoke(String s) {
    return !s.isEmpty() ? LocalDate.parse(s, ofPattern("yyyy-MM-dd")) : null;
  }
}
