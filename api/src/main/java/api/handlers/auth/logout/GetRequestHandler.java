package api.handlers.auth.logout;

import javax.inject.Inject;

import org.pac4j.javalin.LogoutHandler;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;

public class GetRequestHandler implements RequestHandler {
  private final Handler authMiddleware;

  @Inject
  public GetRequestHandler(LogoutHandler authMiddleware) {
    this.authMiddleware = authMiddleware;
  }

  public Handler[] before() {
    return new Handler[] { authMiddleware };
  }

  @Override
  public void handle(Context ctx) throws Exception {
    ctx.status(200).redirect("/");
  }
}
