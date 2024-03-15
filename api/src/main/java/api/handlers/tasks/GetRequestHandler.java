package api.handlers.tasks;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import io.javalin.http.Context;

import api.api.RequestHandler;
import api.api.entities.ApiTask;
import api.providers.logger.LoggerProvider;
import api.tasks.DaggerTasks;

public class GetRequestHandler implements RequestHandler {
  private final Set<ApiTask> apiTasks;
  private final Logger logger;

  @Inject
  public GetRequestHandler(LoggerProvider loggerProvider) {
    logger = loggerProvider.getLogger(getClass());

    // DaggerTasks is a generated class which provides the Set<Task> of all tasks
    // denoted with @Provides @IntoSet
    try {
      var injectedTasks = DaggerTasks.create().tasks();

      var tasks = new ArrayList<ApiTask>();
      for (var t : injectedTasks) {
        tasks.add(new ApiTask().fromTask(t));
      }
      apiTasks = Set.copyOf(tasks);
    } catch (Exception e) {
      logger.error("Failed to get tasks.", e);
      throw e;
    }
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    try {
      ctx.json(apiTasks);
    } catch (Exception e) {
      logger.error("Failed to get tasks.", e);
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
