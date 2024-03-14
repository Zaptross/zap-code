package api.providers.logger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;

@Module
public class SLF4JLoggerProvider implements LoggerProvider {
  @Inject
  public SLF4JLoggerProvider() {
  }

  @SuppressWarnings("rawtypes") // This is only used for logging and does not need to be strictly type safe
  public Logger getLogger(Class clazz) {
    return LoggerFactory.getLogger(clazz);
  }
}
