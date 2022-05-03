package org.example.repository;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("Unknown user");
  }

  public UserNotFoundException(Long id) {
    super("Could not find user with id: " + id);
  }

  public UserNotFoundException(String email) {
    super("Could not find user with email: " + email);
  }
}
