package api.middleware;

import static com.mongodb.client.model.Filters.eq;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.pac4j.core.profile.UserProfile;

import com.mongodb.client.MongoCollection;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.database.entities.User;
import dagger.Module;

@Module
public class UserContextMiddleware implements Handler {
  private final MongoCollection<User> users;

  @Inject
  public UserContextMiddleware(MongoCollection<User> users) {
    this.users = users;
  }

  public void handle(@NotNull Context ctx) {
    var profile = getUserProfileFromContext(ctx);
    if (profile == null) {
      return;
    }

    ctx.sessionAttribute("userProfile", profile);

    var user = users.find(eq("externalId", profile.getId())).first();

    if (user == null) {
      return;
    }

    ctx.sessionAttribute("user", user);
  }

  private UserProfile getUserProfileFromContext(Context ctx) {
    var profileStore = ctx.sessionAttributeMap().get("pac4jUserProfiles");

    if (profileStore instanceof LinkedHashMap) {
      @SuppressWarnings("rawtypes") // We are correctly checking the result of the cast
      var profile = ((LinkedHashMap) profileStore).get("GitHubClient");

      if (profile instanceof UserProfile) {
        return (UserProfile) profile;
      }
    }

    return null;
  }
}
