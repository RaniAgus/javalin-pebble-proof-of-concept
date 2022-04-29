package org.example.controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.validators.ValidationExceptionFactory;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import static java.util.Optional.ofNullable;

public class BaseRepository implements WithGlobalEntityManager, TransactionalOps {
  protected UploadedFile getUploadedFile(Context ctx, String key) {
    return ofNullable(ctx.uploadedFile(key))
        .orElseThrow(() -> ValidationExceptionFactory.nullcheckFailed(key));
  }
}
