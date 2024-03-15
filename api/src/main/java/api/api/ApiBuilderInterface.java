package api.api;

import io.javalin.Javalin;

public interface ApiBuilderInterface {
  void apply(Javalin app);
}
