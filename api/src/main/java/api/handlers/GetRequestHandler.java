package api.handlers;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;

import api.api.RequestHandler;

public class GetRequestHandler implements RequestHandler {
  @Inject
  public GetRequestHandler() {
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    ctx.result("Hello, World!");
  }
}
