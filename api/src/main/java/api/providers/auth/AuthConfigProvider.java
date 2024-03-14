package api.providers.auth;

import javax.inject.Singleton;

import org.pac4j.core.config.Config;
import org.pac4j.core.profile.factory.ProfileManagerFactory;
import org.pac4j.jee.context.JEEContextFactory;
import org.pac4j.jee.context.session.JEESessionStoreFactory;
import org.pac4j.oauth.client.GitHubClient;

import api.config.OAuth2Config;
import dagger.Module;
import dagger.Provides;

@Module
public class AuthConfigProvider {
  @Singleton
  @Provides
  public static Config provideOAuth2ClientConfig(OAuth2Config config) {
    config.forProvider("github");
    var ghc = new GitHubClient(config.clientId, config.clientSecret);
    var cfg = new Config(config.redirectUri, ghc);

    cfg.setWebContextFactory(JEEContextFactory.INSTANCE);
    cfg.setSessionStoreFactory(JEESessionStoreFactory.INSTANCE);
    cfg.setProfileManagerFactory(ProfileManagerFactory.DEFAULT);

    return cfg;
  }
}
