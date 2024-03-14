package api.handlers.auth.login;

import javax.inject.Inject;

import org.pac4j.javalin.SecurityHandler;
import org.slf4j.Logger;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.providers.logger.LoggerProvider;

public class GetRequestHandler implements RequestHandler {
  public final Handler authMiddleware;
  public final Logger logger;

  @Inject
  public GetRequestHandler(SecurityHandler authMiddleware, LoggerProvider loggerProvider) {
    this.authMiddleware = authMiddleware;
    this.logger = loggerProvider.getLogger(getClass());
  }

  public Handler[] before() {
    return new Handler[] { authMiddleware };
  }

  @Override
  public void handle(Context ctx) throws Exception {
    var profile = getUserProfileFromContext(ctx);
    if (profile != null) {
      ctx.redirect("/");
      return;
    }

    ctx.result("Authenticated!");
  }
}
