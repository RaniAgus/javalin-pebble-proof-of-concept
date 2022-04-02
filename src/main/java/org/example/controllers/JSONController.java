package org.example.controllers;

import io.javalin.http.Context;
import org.example.dao.UserRepository;

public abstract class JSONController {
  public static void getAll(Context ctx) {
    ctx.json(UserRepository.findAll());
  }

  public static void getOne(Context ctx) {
    ctx.json(UserRepository.findFirstById(ctx.pathParam("user-id")));
  }
}
