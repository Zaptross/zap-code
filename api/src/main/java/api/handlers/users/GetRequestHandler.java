package api.handlers.users;

import static com.mongodb.client.model.Filters.eq;

import java.util.Set;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import com.mongodb.client.MongoCollection;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.entities.ApiUser;
import api.database.entities.User;
import api.tasks.DaggerTasks;
import api.tasks.Task;

public class GetRequestHandler implements Handler {
  public final Set<Task> tasks;
  public final MongoCollection<User> users;

  @Inject
  public GetRequestHandler(MongoCollection<User> users) {
    this.tasks = DaggerTasks.create().tasks();
    this.users = users;
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    var email = ctx.queryParam("email");

    if (email == null) {
      ctx.status(400).result("No email provided.");
      return;
    }

    var user = users.find(eq("email", email)).first();

    if (user == null) {
      ctx.status(404).result("User not found.");
      return;
    }

    ctx.json(ApiUser.fromUser(user));
  }
}
