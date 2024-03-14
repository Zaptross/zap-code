package api.providers.logger;

import org.slf4j.Logger;

public interface LoggerProvider {
  @SuppressWarnings("rawtypes") // This is only used for logging and does not need to be strictly type safe
  public Logger getLogger(Class clazz);
}