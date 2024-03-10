package api.handlers.solutions._id;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.ApiJob;
import api.middleware.AuthMiddleware;
import api.providers.jobs.JobProvider;

public class GetRequestHandler implements RequestHandler {
  private final JobProvider jobProvider;
  private final Handler authMiddleware;

  @Inject
  public GetRequestHandler(JobProvider jobProvider, AuthMiddleware authMiddleware) {
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
      var jobId = ctx.pathParam("id");

      if (jobId == null || !ObjectId.isValid(jobId)) {
        ctx.status(400).result("Missing or invalid job id provided.");
        return;
      }

      var oid = new ObjectId(jobId);
      var job = jobProvider.GetJob(oid);

      if (job == null) {
        ctx.status(404).result("Job not found.");
        return;
      }

      ctx.json(ApiJob.fromJob(job));
    } catch (Exception e) {
      ctx.status(500).result("Something went wrong while processing your request.");
    }
  }
}
