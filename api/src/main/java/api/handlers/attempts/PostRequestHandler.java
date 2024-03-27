package api.handlers.attempts;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.pac4j.javalin.SecurityHandler;
import org.slf4j.Logger;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.JobRequest;
import api.database.entities.User;
import api.middleware.UserContextMiddleware;
import api.providers.jobs.JobProvider;
import api.providers.logger.LoggerProvider;

public class PostRequestHandler implements RequestHandler {
  private final JobProvider jobProvider;
  private final Handler authMiddleware;
  private final Handler userContextMiddleware;
  private final Logger logger;

  private class Response {
    @SuppressWarnings("unused") // Used for JSON serialization
    public String attemptId;

    public Response(String attemptId) {
      this.attemptId = attemptId;
    }
  }

  @Inject
  public PostRequestHandler(JobProvider jobProvider, SecurityHandler authMiddleware,
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
      var j = ctx.bodyAsClass(JobRequest.class);

      if (j.taskId == null || j.solution == null) {
        ctx.status(400).result("Missing required fields.");
        return;
      }

      var job = j.toJob();
      job.userId = user.id;
      jobProvider.SubmitJob(job);

      ctx.status(201).json(new Response(job.id.toString()));
    } catch (Exception e) {
      logger.error("Failed to submit solution for task.", e);
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
