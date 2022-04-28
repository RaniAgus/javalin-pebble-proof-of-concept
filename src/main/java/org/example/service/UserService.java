package org.example.service;

import org.example.data.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService {
  private static final List<User> users = new ArrayList<>();

  static {
    users.add(new User("1", "Katniss", "Everdeen", LocalDate.now(), "Female", "keverdeen@gmail.com"));
    users.add(new User("2", "Tyrion", "Lannister", LocalDate.now(), "Male", "lann_the_man@gmail.com"));
    users.add(new User("3", "Hank", "Schrader", LocalDate.now(), "Male", "minerals@marie.com"));
    users.add(new User("4", "Steve", "Holt", null, "Male", "steve@holt.com"));
    users.add(new User("5", "Rick", "O'Connell", LocalDate.now(), "Male", "themummy@gmail.com"));
    users.add(new User("6", "Samwise", "Gamgee", null, "Male", "sammie_3@gmail.com"));
    users.add(new User("7", "Elaine", "Benes", LocalDate.now(), "Female", "e_ben@hotmail.com"));
    users.add(new User("8", "Kenny", "Powers", LocalDate.now(), "Male", "f_this_noise@aol.com"));
  }

  public List<User> getUsers() {
    return users;
  }

  public User getUser(String id) {
    return users.stream()
        .filter(user -> user.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(id));
  }
}
