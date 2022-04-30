package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.auth.JWT;
import org.example.validators.ValidationExceptionFactory;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.util.Map;

import static java.util.Optional.ofNullable;

public class BaseController implements WithGlobalEntityManager, TransactionalOps {
  protected UploadedFile getUploadedFile(Context ctx, String key) {
    return ofNullable(ctx.uploadedFile(key))
        .orElseThrow(() -> ValidationExceptionFactory.nullcheckFailed(key));
  }

  protected void render(Context ctx, String template, Map<String, Object> model) {
    model.put("userId", JWT.verify(ctx.cookie("session")));
    ctx.render(template, model);
  }
}
