package api.handlers.logs;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;

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

  private class Log {
    public final String id;
    public final String taskId;

    public Log(Logs log) {
      this.id = log.id.toHexString();
      this.taskId = log.taskId.toHexString();
    }
  }

  @Inject
  public GetRequestHandler(MongoCollection<Logs> logs, SecurityHandler authMiddleware,
      UserContextMiddleware userContextMiddleware,
      LoggerProvider loggerProvider) {
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
      var queryTaskId = ctx.queryParam("taskId");
      var filter = getFilter(user.id, queryTaskId);

      if (filter == null) {
        ctx.status(400).result("Missing taskId query parameter.");
        return;
      }

      var logs = this.logs
          .find(filter)
          .limit(100)
          .into(new ArrayList<Logs>());

      var apiLogs = logs.stream().map(log -> new Log(log)).toList();

      ctx.json(apiLogs);
    } catch (Exception e) {
      logger.error("Failed to get log by id.", e);
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }

  private Bson getFilter(ObjectId userId, String taskId) {
    if (!Utils.IsValidOID(taskId)) {
      return eq("userId", userId);
    }
    return and(eq("userId", userId), eq("taskId", new ObjectId(taskId)));
  }
}
