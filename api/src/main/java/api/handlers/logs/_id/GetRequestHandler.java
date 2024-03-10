package api.handlers.logs._id;

import static com.mongodb.client.model.Filters.eq;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import com.mongodb.client.MongoCollection;

import io.javalin.http.Context;

import api.api.RequestHandler;
import api.api.entities.ApiLogs;
import api.database.entities.Logs;

public class GetRequestHandler implements RequestHandler {
  public final MongoCollection<Logs> logs;

  @Inject
  public GetRequestHandler(MongoCollection<Logs> logs) {
    this.logs = logs;
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    try {
      var runId = ctx.pathParam("id");

      if (runId == null || !ObjectId.isValid(runId)) {
        ctx.status(400).result("Missing or invalid run id provided.");
        return;
      }

      var oid = new ObjectId(runId);
      var log = logs.find(eq("_id", oid)).first();

      if (log == null || log.id == null) {
        ctx.status(404).result("Log not found.");
        return;
      }

      ctx.json(ApiLogs.fromLogs(log));
    } catch (Exception e) {
      ctx.status(500).result("Something went wrong trying to process your request.");
    }
  }
}
