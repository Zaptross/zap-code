package api.handlers;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class GetRequestHandler implements Handler {
  @Inject
  public GetRequestHandler() {
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    ctx.result("Hello, World!");
  }
}
