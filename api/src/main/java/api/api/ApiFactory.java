package api.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.javalin.Javalin;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiFactory {
  @Inject
  @Singleton
  @Provides
  public Javalin createJavalin() {
    return Javalin.create();
  }
}
