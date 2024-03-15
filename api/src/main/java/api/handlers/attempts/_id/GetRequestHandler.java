package api.handlers.attempts._id;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.pac4j.javalin.SecurityHandler;
import org.slf4j.Logger;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.ApiJob;
import api.database.entities.User;
import api.middleware.UserContextMiddleware;
import api.providers.jobs.JobProvider;
import api.providers.logger.LoggerProvider;

public class GetRequestHandler implements RequestHandler {
  private final JobProvider jobProvider;
  private final Logger logger;
  private final Handler authMiddleware;
  private final Handler userContextMiddleware;

  @Inject
  public GetRequestHandler(JobProvider jobProvider, SecurityHandler authMiddleware,
      UserContextMiddleware userContextMiddleware, LoggerProvider loggerProvider) {
    this.jobProvider = jobProvider;
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
      var jobId = ctx.pathParam("id");

      if (jobId == null || !ObjectId.isValid(jobId)) {
        ctx.status(400).result("Missing or invalid job id provided.");
        return;
      }

      var oid = new ObjectId(jobId);
      var job = jobProvider.GetJob(oid, user.id);

      if (job == null) {
        ctx.status(404).result("Job not found.");
        return;
      }

      ctx.json(ApiJob.fromJob(job));
    } catch (Exception e) {
      logger.error("Failed to get solution by id.", e);
      ctx.status(500).result("Something went wrong while processing your request.");
    }
  }
}
