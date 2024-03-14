package api.config;

import javax.inject.Inject;

import io.github.cdimascio.dotenv.Dotenv;

import api.env.EnvConfig;
import dagger.Module;

@Module
public class OAuth2Config {
  public String name;
  public String clientId;
  public String clientSecret;
  public String discoveryUri;
  public String redirectUri;
  public String tokenUri;
  public String authUri;
  public String scopes;

  private final Dotenv env;

  @Inject
  public OAuth2Config(Dotenv env) {
    this.env = env;
  }

  public OAuth2Config forProvider(String provider) {
    return new EnvConfig<OAuth2Config>(OAuth2Config.class, this, provider)
        .FromEnv(env)
        .BuildWithPublicFields();
  }
}
