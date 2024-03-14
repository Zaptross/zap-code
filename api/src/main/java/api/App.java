package api;

import javax.inject.Singleton;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.javalin.Javalin;

import api.api.ApiFactory;
import api.config.DotenvModule;
import api.database.MongoFactory;
import api.database.entities.User;
// generated imports
import api.generated.CollectionFactory;
import api.generated.LogsIdApiBuilder;
import api.generated.RootApiBuilder;
import api.generated.SolutionsApiBuilder;
import api.generated.SolutionsIdApiBuilder;
import api.generated.TasksApiBuilder;
import api.generated.UsersEmailApiBuilder;
import api.providers.jobs.JobProviderModule;
import api.providers.logger.LoggerProviderModule;
import dagger.Component;

public class App {
    @Singleton
    @Component(modules = {
            DotenvModule.class,
            MongoFactory.class,
            ApiFactory.class,
            LoggerProviderModule.class,
            JobProviderModule.class, // generated
            CollectionFactory.class, // generated
            RootApiBuilder.class, // generated
            LogsIdApiBuilder.class, // generated
            SolutionsApiBuilder.class, // generated
            SolutionsIdApiBuilder.class, // generated
            TasksApiBuilder.class, // generated
            UsersEmailApiBuilder.class, // generated
    })

    public interface AppBuilder {
        MongoDatabase database();

        MongoCollection<User> userCollection();

        RootApiBuilder rootApiBuilder();

        LogsIdApiBuilder logsIdApiBuilder();

        SolutionsApiBuilder solutionsApiBuilder();

        SolutionsIdApiBuilder solutionsIdApiBuilder();

        TasksApiBuilder tasksApiBuilder();

        UsersEmailApiBuilder usersEmailApiBuilder();

        Javalin javalin();

        JavalinConfig config();
    }

    public String getGreeting() {
        return "Hello World";
    }

    public static void main(String[] args) {
        AppBuilder api = DaggerApp_AppBuilder
                .builder()
                .build();

        var app = api.javalin();
        api.rootApiBuilder().apply();
        api.logsIdApiBuilder().apply();
        api.solutionsApiBuilder().apply();
        api.solutionsIdApiBuilder().apply();
        api.tasksApiBuilder().apply();
        api.usersEmailApiBuilder().apply();
        app.start(api.config().getPort());
    }
}