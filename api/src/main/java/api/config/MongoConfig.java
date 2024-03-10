package api.config;

import javax.inject.Inject;

import io.github.cdimascio.dotenv.Dotenv;

import api.env.EnvConfig;

public class MongoConfig {
  public String User;
  public String Password;
  public String Host;
  public String Port;
  public String Database;

  @Inject
  public MongoConfig(Dotenv env) {
    new EnvConfig<MongoConfig>(MongoConfig.class, this, "database")
        .FromEnv(env)
        .BuildWithPublicFields();
  }

  public String ToConnectionString() {
    return String.format(
        "mongodb://%s:%s@%s:%s/",
        this.User,
        this.Password,
        this.Host,
        this.Port);
  }
}
