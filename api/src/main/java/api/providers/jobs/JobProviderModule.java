package api.providers.jobs;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public interface JobProviderModule {
  @Singleton
  @Binds
  JobProvider provideJobProvider(DatabaseJobProvider provider);
}
