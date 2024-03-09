package api.handlers.tasks;

import java.util.Set;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import com.mongodb.client.MongoCollection;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.entities.JobRequest;
import api.database.entities.Job;
import api.tasks.DaggerTasks;
import api.tasks.Task;

public class PostRequestHandler implements Handler {
  public final Set<Task> tasks;
  public final MongoCollection<Job> jobs;

  @Inject
  public PostRequestHandler(MongoCollection<Job> jobs) {
    this.tasks = DaggerTasks.create().tasks();
    this.jobs = jobs;
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
      jobs.insertOne(job);

      ctx.status(201).result(job.id.toString());
    } catch (Exception e) {
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
