package api.handlers.tasks;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import api.api.entities.ApiTask;
import api.tasks.DaggerTasks;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class GetRequestHandler implements Handler {
  public final Set<ApiTask> apiTasks;

  @Inject
  public GetRequestHandler() {
    // DaggerTasks is a generated class which provides the Set<Task> of all tasks
    // denoted with @Provides @IntoSet
    var injectedTasks = DaggerTasks.create().tasks();

    var tasks = new ArrayList<ApiTask>();
    for (var t : injectedTasks) {
      tasks.add(new ApiTask().fromTask(t));
    }
    apiTasks = Set.copyOf(tasks);
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    ctx.json(apiTasks);
  }
}
