package api.handlers.auth.callback;

import static com.mongodb.client.model.Filters.eq;

import java.util.LinkedHashMap;

import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.javalin.CallbackHandler;
import org.slf4j.Logger;

import com.mongodb.client.MongoCollection;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import api.api.RequestHandler;
import api.database.entities.User;
import api.database.enums.AuthProvider;
import api.providers.logger.LoggerProvider;

public class GetRequestHandler implements RequestHandler {
  public final Handler authMiddleware;
  public final MongoCollection<User> userCollection;
  public final Logger logger;

  @Inject
  public GetRequestHandler(CallbackHandler authMiddleware,
      LoggerProvider loggerProvider,
      MongoCollection<User> userCollection) {
    this.authMiddleware = authMiddleware;
    this.userCollection = userCollection;
    this.logger = loggerProvider.getLogger(getClass());
  }

  @Override
  public Handler[] before() {
    return new Handler[] { authMiddleware };
  }

  @Override
  public void handle(Context ctx) throws Exception {
  }

  @Override
  public Handler[] after() {
    return new Handler[] { c -> saveOrUpdateUser(c) };
  }

  private void saveOrUpdateUser(Context ctx) {
    var profile = getUserProfileFromContext(ctx);
    if (profile == null) {
      return;
    }

    var user = userCollection.find(eq("externalId", profile.getId())).first();

    if (user != null) {
      // Update user's username, avatar, and mfa / security status
      user.avatarUrl = profile.getAttribute("avatar_url").toString();
      user.username = profile.getUsername();
      userCollection.replaceOne(eq("id", user.id), user);
      return;
    }

    // Create a new user
    var newUser = new User();
    newUser.id = new ObjectId();
    newUser.authProvider = AuthProvider.GITHUB;
    newUser.externalId = profile.getId();
    newUser.username = profile.getUsername();
    newUser.avatarUrl = profile.getAttribute("avatar_url").toString();
    userCollection.insertOne(newUser);
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
