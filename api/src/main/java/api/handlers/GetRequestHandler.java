package api.handlers;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import io.javalin.http.Context;

import api.api.RequestHandler;
import api.providers.logger.LoggerProvider;

public class GetRequestHandler implements RequestHandler {
  private final Logger logger;

  @Inject
  public GetRequestHandler(LoggerProvider loggerProvider) {
    this.logger = loggerProvider.getLogger(getClass());
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    try {
      ctx.result("Hello, World!");
    } catch (Exception e) {
      logger.error("Failed to respond to request.", e);
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
