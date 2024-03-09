package api;

import static com.mongodb.client.model.Filters.eq;

import javax.inject.Singleton;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.javalin.Javalin;

import api.api.ApiFactory;
import api.config.DotenvModule;
import api.database.MongoFactory;
import api.database.entities.User;
import api.database.enums.AuthProvider;
// generated imports
import api.generated.CollectionFactory;
import api.generated.LogsApiBuilder;
import api.generated.TasksApiBuilder;
import api.generated.UsersApiBuilder;
import api.middleware.AuthHandler;
import dagger.Component;

public class App {
    @Singleton
    @Component(modules = {
            DotenvModule.class,
            MongoFactory.class,
            ApiFactory.class,
            CollectionFactory.class, // generated
            TasksApiBuilder.class, // generated
            UsersApiBuilder.class, // generated
            LogsApiBuilder.class, // generated
    })

    public interface AppBuilder {
        MongoDatabase database();

        MongoCollection<User> userCollection();

        TasksApiBuilder tasksApiBuilder();

        UsersApiBuilder usersApiBuilder();

        LogsApiBuilder logsApiBuilder();

        Javalin javalin();
    }

    public String getGreeting() {
        return "Hello World";
    }

    public static void main(String[] args) {

        AppBuilder api = DaggerApp_AppBuilder
                .builder()
                .build();

        var app = api.javalin();
        api.tasksApiBuilder().apply();
        api.usersApiBuilder().apply();
        api.logsApiBuilder().apply();
        app.start(7070);

        try {
            User user = api.userCollection().find(eq("email", "test@testing.testing")).first();

            if (user == null) {
                user = new User();
                user.id = ObjectId.get();
                user.authProvider = AuthProvider.GITHUB;
                user.email = "test@testing.testing";

                var res = api.userCollection().insertOne(user);
                res.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        app.before("/users/<email>", new AuthHandler());
        app.get("/users/<email>", ctx -> {
            try {
                var collection = api.userCollection();
                User foundUser = collection.find(eq("email", ctx.pathParam("email"))).first();
                ctx.json(foundUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        app.get("/<name>", ctx -> ctx.result("Hello " + ctx.pathParam("name")));
    }
}