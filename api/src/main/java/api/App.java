package api;

import javax.inject.Singleton;

import io.javalin.Javalin;

import api.api.ApiProvider;
import api.config.DotenvModule;
import api.config.JavalinConfig;
import api.config.OAuth2Config;
import api.database.MongoFactory;
import api.generated.ApiBuildersApplicator;
import api.generated.CollectionFactory;
import api.providers.auth.AuthConfigProvider;
import api.providers.auth.CallbackHandlerProvider;
import api.providers.auth.LogoutHandlerProvider;
import api.providers.auth.SecurityHandlerProvider;
import api.providers.jobs.JobProviderModule;
import api.providers.logger.LoggerProviderModule;
import dagger.Component;

public class App {
    @Singleton
    @Component(modules = {
            DotenvModule.class,
            MongoFactory.class,
            ApiProvider.class,
            LoggerProviderModule.class,
            AuthConfigProvider.class,
            OAuth2Config.class,
            SecurityHandlerProvider.class,
            LogoutHandlerProvider.class,
            CallbackHandlerProvider.class,
            JobProviderModule.class, // generated
            CollectionFactory.class, // generated
            ApiBuildersApplicator.class // generated
    })

    public interface AppBuilder {
        Javalin javalin();

        JavalinConfig config();

        ApiBuildersApplicator apiBuildersApplicator();
    }

    public String getGreeting() {
        return "Hello World";
    }

    public static void main(String[] args) {
        AppBuilder api = DaggerApp_AppBuilder
                .builder()
                .build();

        var app = api.javalin();
        api.apiBuildersApplicator().applyAll(app);
        app.start(api.config().getPort());
    }
}