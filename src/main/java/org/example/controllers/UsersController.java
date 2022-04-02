package org.example.controllers;

import io.javalin.http.Context;
import org.example.dao.UserRepository;
import org.example.dto.UserDTO;
import org.example.model.User;

import java.util.Set;

import static java.util.stream.Collectors.toList;

public abstract class UsersController {
  public static void getAll(Context ctx) {
    Set<User> users = UserRepository.findAll();
    ctx.json(users.stream().map(UserDTO::of).collect(toList()));
  }

  public static void getOne(Context ctx) {
    User user = UserRepository.findFirstById(ctx.pathParam("user-id"));
    ctx.json(UserDTO.of(user));
  }
}
