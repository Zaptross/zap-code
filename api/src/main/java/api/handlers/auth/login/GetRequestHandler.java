package api.handlers.auth.login;

import javax.inject.Inject;

import org.pac4j.javalin.SecurityHandler;
import org.slf4j.Logger;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.middleware.UserContextMiddleware;
import api.providers.logger.LoggerProvider;

public class GetRequestHandler implements RequestHandler {
  private final Handler authMiddleware;
  private final Handler userContextMiddleware;
  private final Logger logger;

  @Inject
  public GetRequestHandler(SecurityHandler authMiddleware, UserContextMiddleware userContextMiddleware,
      LoggerProvider loggerProvider) {
    this.authMiddleware = authMiddleware;
    this.userContextMiddleware = userContextMiddleware;
    this.logger = loggerProvider.getLogger(getClass());
  }

  public Handler[] before() {
    return new Handler[] { authMiddleware, userContextMiddleware };
  }

  @Override
  public void handle(Context ctx) throws Exception {
    var user = ctx.sessionAttribute("user");
    if (user != null) {
      ctx.redirect("/");
      return;
    }

    ctx.result("Authenticated!");
  }
}
