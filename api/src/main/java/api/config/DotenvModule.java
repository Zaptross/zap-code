package api.config;

import dagger.Module;
import dagger.Provides;
import io.github.cdimascio.dotenv.Dotenv;

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
