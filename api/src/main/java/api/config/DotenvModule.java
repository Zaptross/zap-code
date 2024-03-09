package api.config;

import io.github.cdimascio.dotenv.Dotenv;

import dagger.Module;
import dagger.Provides;

@Module
public class DotenvModule {
  @Provides
  static Dotenv provideEnvConfig() {
    return Dotenv
        .configure()
        .systemProperties()
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();
  }
}
