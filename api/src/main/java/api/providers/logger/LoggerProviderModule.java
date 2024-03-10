package api.providers.logger;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface LoggerProviderModule {
  @Singleton
  @Binds
  LoggerProvider provideLoggerProvider(SLF4JLoggerProvider provider);
}