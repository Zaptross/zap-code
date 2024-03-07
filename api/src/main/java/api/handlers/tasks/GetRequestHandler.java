package api.handlers.tasks;

import java.util.Set;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import api.tasks.DaggerTasks;
import api.tasks.Task;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class GetRequestHandler implements Handler {
  public final Set<Task> tasks;

  @Inject
  public GetRequestHandler() {
    this.tasks = DaggerTasks.create().tasks();
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    ctx.json(tasks);
  }
}
