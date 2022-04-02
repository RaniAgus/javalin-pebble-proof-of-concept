package org.example.dao;

import io.javalin.http.Context;
import org.example.dto.UserDTO;
import org.example.model.User;
import org.example.model.UserDetails;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

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

  public static void getAll(Context ctx) {
    ctx.json(users.stream().map(UserDTO::of).collect(toList()));
  }

  public static void getOne(Context ctx) {
    User user = users.stream()
        .filter(u -> u.getId().equals(ctx.pathParam("user-id")))
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(ctx.pathParam("user-id")));

    ctx.json(UserDTO.of(user));
  }
}
