package api.handlers.attempts;

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
import api.database.entities.Job;
import api.database.entities.User;
import api.middleware.UserContextMiddleware;
import api.providers.logger.LoggerProvider;
import api.utils.Utils;

public class GetRequestHandler implements RequestHandler {
  private final MongoCollection<Job> jobs;
  private final Logger logger;
  private final Handler authMiddleware;
  private final Handler userContextMiddleware;

  @SuppressWarnings("unused")
  private class Attempt {
    public final String id;
    public final String taskId;
    public final String solution;

    public Attempt(Job job) {
      this.id = job.id.toHexString();
      this.taskId = job.taskId.toHexString();
      this.solution = job.solution;
    }
  }

  @Inject
  public GetRequestHandler(MongoCollection<Job> jobs, SecurityHandler authMiddleware,
      UserContextMiddleware userContextMiddleware, LoggerProvider loggerProvider) {
    this.jobs = jobs;
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
      var taskId = ctx.queryParam("taskId");

      if (!Utils.IsValidOID(taskId)) {
        ctx.status(400).result("Missing or invalid job id provided.");
        return;
      }

      var oid = new ObjectId(taskId);

      var foundJobs = jobs.find(getFilter(user.id, oid)).into(new ArrayList<Job>());

      if (foundJobs.size() == 0) {
        ctx.status(404).result("Job not found.");
        return;
      }

      ctx.json(foundJobs.stream().map(job -> new Attempt(job)).toList());
    } catch (Exception e) {
      logger.error("Failed to get solution by id.", e);
      ctx.status(500).result("Something went wrong while processing your request.");
    }
  }

  private Bson getFilter(ObjectId userId, ObjectId taskId) {
    return and(eq("userId", userId), eq("taskId", taskId));
  }
}
