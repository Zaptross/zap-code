package api.handlers.solutions;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.JobRequest;
import api.middleware.AuthMiddleware;
import api.providers.jobs.JobProvider;
import api.providers.logger.LoggerProvider;

public class PostRequestHandler implements RequestHandler {
  public final JobProvider jobProvider;
  public final Handler authMiddleware;
  public final Logger logger;

  @Inject
  public PostRequestHandler(JobProvider jobProvider, AuthMiddleware authMiddleware, LoggerProvider loggerProvider) {
    this.jobProvider = jobProvider;
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
      var j = ctx.bodyAsClass(JobRequest.class);

      if (j.userId == null || j.taskId == null || j.solution == null) {
        ctx.status(400).result("Missing required fields.");
        return;
      }

      var job = j.toJob();
      jobProvider.SubmitJob(job);

      ctx.status(201).result(job.id.toString());
    } catch (Exception e) {
      logger.error("Failed to submit solution for task.", e);
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
