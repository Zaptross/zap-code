package api.handlers.user;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.pac4j.javalin.SecurityHandler;
import org.slf4j.Logger;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.ApiUser;
import api.database.entities.User;
import api.middleware.UserContextMiddleware;
import api.providers.logger.LoggerProvider;

public class GetRequestHandler implements RequestHandler {
  private final Handler authMiddleware;
  private final Handler userContextMiddleware;
  private final Logger logger;

  @Inject
  public GetRequestHandler(SecurityHandler authMiddleware,
      UserContextMiddleware userContextMiddleware,
      LoggerProvider loggerProvider) {
    this.authMiddleware = authMiddleware;
    this.userContextMiddleware = userContextMiddleware;
    this.logger = loggerProvider.getLogger(getClass());
  }

  @Override
  public Handler[] before() {
    return new Handler[] { authMiddleware, userContextMiddleware };
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    try {
      var user = ctx.sessionAttribute("user");
      if (user == null) {
        ctx.status(401).result("Unauthorized");
        return;
      }

      ctx.json(ApiUser.fromUser((User) user));
    } catch (Exception e) {
      logger.error("Failed to respond to request.", e);
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
