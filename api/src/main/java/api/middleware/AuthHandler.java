package api.middleware;

import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class AuthHandler implements Handler {

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    var token = ctx.header("Authorization");
    if (token == null) {
      ctx.status(401).result("Unauthorized");
      ctx.skipRemainingHandlers();
      return;
    }
    if (!token.equals("Bearer 123")) {
      ctx.status(403).result("Forbidden");
      ctx.skipRemainingHandlers();
      return;
    }
  }
}
