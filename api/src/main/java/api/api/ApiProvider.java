package api.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.javalin.Javalin;

import api.config.JavalinConfig;
import dagger.Module;
import dagger.Provides;

@Module
public class ApiProvider {
  @Inject
  @Singleton
  @Provides
  public Javalin createJavalin(JavalinConfig config) {
    return Javalin.create(cfg -> {
      cfg.plugins.enableCors(cors -> {
        cors.add(corsCfg -> {
          corsCfg.allowHost(config.webapp);
          corsCfg.allowCredentials = true;
          for (var origin : config.getCors()) {
            corsCfg.allowHost(origin);
          }
        });
      });
    });
  }
}
