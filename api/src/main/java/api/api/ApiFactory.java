package api.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.javalin.Javalin;

@Module
public class ApiFactory {
  @Inject
  @Singleton
  @Provides
  public Javalin createJavalin() {
    return Javalin.create();
  }
}
