package api.providers.auth;

import org.pac4j.core.config.Config;
import org.pac4j.javalin.SecurityHandler;

import dagger.Module;
import dagger.Provides;

@Module
public class SecurityHandlerProvider {
  @Provides
  public static SecurityHandler provideSecurityHandler(Config config) {
    return new SecurityHandler(config, "GitHubClient");
  }
}
