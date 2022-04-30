package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.auth.JWT;
import org.example.validators.ValidationExceptionFactory;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import static io.javalin.core.util.FileUtil.streamToFile;
import static java.util.Optional.ofNullable;

public class BaseController implements WithGlobalEntityManager, TransactionalOps {
  protected UploadedFile getUploadedFile(Context ctx, String key) {
    return ofNullable(ctx.uploadedFile(key))
        .orElseThrow(() -> ValidationExceptionFactory.nullcheckFailed(key));
  }

  protected void saveFile(UploadedFile file, String name) {
    streamToFile(file.getContent(), "static/images/" + name);
  }

  protected Long getSession(Context ctx) {
    return JWT.verify(ctx.cookie("session"));
  }

  protected void setSession(Context ctx, Long id) {
    ctx.cookie("session", JWT.sign(id));
  }

  protected void unsetSession(Context ctx) {
    ctx.removeCookie("session");
  }
}

