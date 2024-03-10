package api.handlers.solutions;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.JobRequest;
import api.middleware.AuthMiddleware;
import api.providers.jobs.JobProvider;

public class PostRequestHandler implements RequestHandler {
  public final JobProvider jobProvider;
  public final Handler authMiddleware;

  @Inject
  public PostRequestHandler(JobProvider jobProvider, AuthMiddleware authMiddleware) {
    this.jobProvider = jobProvider;
    this.authMiddleware = authMiddleware;
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
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
