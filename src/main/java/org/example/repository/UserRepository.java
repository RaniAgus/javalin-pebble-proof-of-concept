package org.example.repository;

import org.example.data.Role;
import org.example.data.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
  private Long id = 0L;
  private final List<User> users = new ArrayList<>();

  public UserRepository() {
    persist(new User("Katniss", "Everdeen", LocalDate.now(), "Female", "keverdeen@gmail.com", Role.STANDARD_USER));
    persist(new User("Tyrion", "Lannister", LocalDate.now(), "Male", "lann_the_man@gmail.com", Role.STANDARD_USER));
    persist(new User("Hank", "Schrader", LocalDate.now(), "Male", "minerals@marie.com", Role.ADMIN));
    persist(new User("Steve", "Holt", null, "Male", "steve@holt.com", Role.STANDARD_USER));
    persist(new User("Rick", "O'Connell", LocalDate.now(), "Male", "themummy@gmail.com", Role.STANDARD_USER));
    persist(new User("Samwise", "Gamgee", null, "Male", "sammie_3@gmail.com", Role.STANDARD_USER));
    persist(new User("Elaine", "Benes", LocalDate.now(), "Female", "e_ben@hotmail.com", Role.ADMIN));
    persist(new User("Kenny", "Powers", LocalDate.now(), "Male", "f_this_noise@aol.com", Role.STANDARD_USER));
  }

  public User getById(Long id) {
    if (id == null) {
      throw new UserNotFoundException();
    }

    return users.stream()
        .filter(it -> it.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  public User getByEmail(String email) {
    if (email == null) {
      throw new UserNotFoundException();
    }

    return users.stream()
        .filter(it -> it.getEmail().equals(email))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(email));
  }

  public void update(User user) {
    if (!exists(user.getId())) {
      throw new UserNotFoundException(user.getId());
    }
    users.replaceAll(it -> it.getId().equals(user.getId()) ? user : it);
  }

  private boolean exists(Long id) {
    return users.stream().anyMatch(it -> it.getId().equals(id));
  }

  private void persist(User user) {
    user.setId(++id);
    users.add(user);
  }
}
