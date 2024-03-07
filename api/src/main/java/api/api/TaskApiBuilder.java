package api.api;

import javax.inject.Inject;

import api.handlers.tasks.GetRequestHandler;
import dagger.Module;
import io.javalin.Javalin;

@Module
public class TaskApiBuilder {
  private final Javalin router;
  private final String basePath = "/tasks";
  private final GetRequestHandler get;

  @Inject
  public TaskApiBuilder(Javalin router, GetRequestHandler get) {
    this.router = router;
    this.get = get;
  }

  public void apply() {
    router.get(basePath, get);
  }
}
