package api.handlers.auth.logout;

import javax.inject.Inject;

import org.pac4j.javalin.LogoutHandler;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.config.JavalinConfig;

public class GetRequestHandler implements RequestHandler {
  private final Handler authMiddleware;
  private final String webappUrl;

  @Inject
  public GetRequestHandler(LogoutHandler authMiddleware, JavalinConfig config) {
    this.authMiddleware = authMiddleware;
    this.webappUrl = config.webapp;
  }

  public Handler[] before() {
    return new Handler[] { authMiddleware };
  }

  @Override
  public void handle(Context ctx) throws Exception {
    ctx.status(200).redirect(webappUrl);
  }
}
