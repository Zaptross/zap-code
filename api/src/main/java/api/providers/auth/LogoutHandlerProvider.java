package api.providers.auth;

import org.pac4j.core.config.Config;
import org.pac4j.javalin.LogoutHandler;

import dagger.Module;
import dagger.Provides;

@Module
public class LogoutHandlerProvider {
  @Provides
  public static LogoutHandler provideSecurityHandler(Config config) {
    return new LogoutHandler(config);
  }
}
