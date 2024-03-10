package api.api;

import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public interface RequestHandler extends Handler {
  void handle(@NotNull Context ctx) throws Exception;

  default Handler[] before() {
    return new Handler[] {};
  }

  default Handler[] after() {
    return new Handler[] {};
  };
}
