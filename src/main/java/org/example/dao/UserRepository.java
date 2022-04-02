package org.example.dao;

import org.example.model.User;
import org.example.model.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class UserRepository {
  private static final List<User> users = asList(
      new User("1", "John", "john@fake.co", new UserDetails("1964-02-21", "2773 JB")),
      new User("2", "Mary", "mary@fake.co", new UserDetails("1994-05-12", "1222 JB")),
      new User("3", "Dave", "dave@fake.co", new UserDetails("1984-05-01", "1833 JB")),
      new User("4", "Jane", "jane@fake.co", new UserDetails("1989-12-30", "1532 JB")),
      new User("5", "Eric", "eric@fake.co", new UserDetails("1973-09-14", "2131 JB")),
      new User("6", "Gina", "gina@fake.co", new UserDetails("1977-08-16", "1982 JB")),
      new User("7", "Ryan", "ryan@fake.co", new UserDetails("1988-11-07", "1638 JB")),
      new User("8", "Judy", "judy@fake.co", new UserDetails("1959-01-05", "2983 JB"))
  );

  public static List<User> findAll() {
    return new ArrayList<>(users);
  }

  public static User findFirstById(String id) {
    return users.stream()
        .filter(u -> u.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(id));
  }
}
