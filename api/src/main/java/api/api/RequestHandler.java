package api.api;

import java.util.LinkedHashMap;

import org.jetbrains.annotations.NotNull;
import org.pac4j.core.profile.UserProfile;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public interface RequestHandler extends Handler {
  void handle(@NotNull Context ctx) throws Exception;

  default Handler[] before() {
    return new Handler[] {};
  }

  default Handler[] after() {
    return new Handler[] {};
  };

  default UserProfile getUserProfileFromContext(Context ctx) {
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
