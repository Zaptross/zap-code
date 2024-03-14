package api.handlers.user;

import static com.mongodb.client.model.Filters.eq;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.pac4j.javalin.SecurityHandler;
import org.slf4j.Logger;

import com.mongodb.client.MongoCollection;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.ApiUser;
import api.database.entities.User;
import api.providers.logger.LoggerProvider;

public class GetRequestHandler implements RequestHandler {
  private final Handler authMiddleware;
  private final MongoCollection<User> userCollection;
  private final Logger logger;

  @Inject
  public GetRequestHandler(MongoCollection<User> userCollection, SecurityHandler authMiddleware,
      LoggerProvider loggerProvider) {
    this.userCollection = userCollection;
    this.authMiddleware = authMiddleware;
    this.logger = loggerProvider.getLogger(getClass());
  }

  @Override
  public Handler[] before() {
    return new Handler[] { authMiddleware };
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    try {
      var profile = getUserProfileFromContext(ctx);
      if (profile == null) {
        ctx.status(401).result("Unauthorized");
        return;
      }

      var user = userCollection.find(eq("externalId", profile.getId())).first();

      if (user == null) {
        ctx.status(401).result("Unauthorized");
        return;
      }

      ctx.json(ApiUser.fromUser(user));
    } catch (Exception e) {
      logger.error("Failed to respond to request.", e);
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
