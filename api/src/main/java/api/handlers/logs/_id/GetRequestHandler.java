package api.handlers.logs._id;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import javax.inject.Inject;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.pac4j.javalin.SecurityHandler;
import org.slf4j.Logger;

import com.mongodb.client.MongoCollection;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.ApiLogs;
import api.database.entities.Logs;
import api.database.entities.User;
import api.middleware.UserContextMiddleware;
import api.providers.logger.LoggerProvider;
import api.utils.Utils;

public class GetRequestHandler implements RequestHandler {
  private final MongoCollection<Logs> logs;
  private final Handler authMiddleware;
  private final Handler userContextMiddleware;
  private final Logger logger;

  @Inject
  public GetRequestHandler(MongoCollection<Logs> logs, SecurityHandler authMiddleware,
      UserContextMiddleware userContextMiddleware, LoggerProvider loggerProvider) {
    this.logs = logs;
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
      var maybeUser = ctx.sessionAttribute("user");
      if (maybeUser == null) {
        ctx.status(401).result("Unauthorized");
        return;
      }

      var user = (User) maybeUser;
      var runId = ctx.pathParam("id");

      if (runId == null || !ObjectId.isValid(runId)) {
        ctx.status(400).result("Missing or invalid run id provided.");
        return;
      }

      var log = logs.find(getFilter(user.id, runId)).first();

      if (log == null || log.id == null) {
        ctx.status(404).result("Log not found.");
        return;
      }

      ctx.json(ApiLogs.fromLogs(log));
    } catch (Exception e) {
      logger.error("Failed to get log by id.", e);
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }

  private Bson getFilter(ObjectId userId, String runId) {
    if (!Utils.IsValidOID(runId)) {
      return null;
    }

    return and(eq("userId", userId), eq("_id", new ObjectId(runId)));
  }
}
