package api.config;

public class MongoConfig {
  public String User;
  public String Password;
  public String Host;
  public String Port;

  public String ToConnectionString() {
    return String.format(
        "mongodb://%s:%s@%s:%s/",
        this.User,
        this.Password,
        this.Host,
        this.Port);
  }
}
