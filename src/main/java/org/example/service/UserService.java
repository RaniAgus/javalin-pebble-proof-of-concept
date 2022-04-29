package org.example.service;

import org.example.data.User;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.Optional;

public class UserService implements WithGlobalEntityManager {
  public User getUser(Long id) {
    return Optional.ofNullable(entityManager().find(User.class, id))
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  public void putUser(User user) {
    entityManager().merge(user);
  }
}
