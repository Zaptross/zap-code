package api.config;

import javax.inject.Inject;

import io.github.cdimascio.dotenv.Dotenv;

import api.env.EnvConfig;

public class JavalinConfig {
  public String port;

  @Inject
  public JavalinConfig(Dotenv env) {
    new EnvConfig<JavalinConfig>(JavalinConfig.class, this, "api")
        .FromEnv(env)
        .BuildWithPublicFields();
  }

  public int getPort() {
    return Integer.parseInt(port);
  }
}
