package api.providers.auth;

import org.pac4j.core.config.Config;
import org.pac4j.javalin.CallbackHandler;

import dagger.Module;
import dagger.Provides;

@Module
public class CallbackHandlerProvider {
  @Provides
  public static CallbackHandler provideCallbackHandler(Config config) {
    return new CallbackHandler(config, null, true);
  }
}
