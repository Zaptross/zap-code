package api.handlers.users._email;

import static com.mongodb.client.model.Filters.eq;

import java.util.Set;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;

import com.mongodb.client.MongoCollection;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.api.entities.ApiUser;
import api.database.entities.User;
import api.middleware.AuthMiddleware;
import api.tasks.DaggerTasks;
import api.tasks.Task;

public class GetRequestHandler implements RequestHandler {
  public final Set<Task> tasks;
  public final MongoCollection<User> users;
  public final Handler auth;

  @Inject
  public GetRequestHandler(MongoCollection<User> users, AuthMiddleware auth) {
    this.tasks = DaggerTasks.create().tasks();
    this.users = users;
    this.auth = auth;
  }

  @Override
  public Handler[] before() {
    return new Handler[] { auth };
  }

  @Override
  public void handle(@NotNull Context ctx) throws Exception {
    var email = ctx.pathParam("email");

    if (email == null || email.isBlank()) {
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
