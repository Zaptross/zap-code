package api;

import static com.mongodb.client.model.Filters.eq;

import javax.inject.Singleton;

import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.javalin.Javalin;
import api.database.entities.User;
import api.database.enums.AuthProvider;
import api.middleware.AuthHandler;
import dagger.Component;
import api.api.ApiFactory;
import api.api.TaskApiBuilder;
import api.config.DotenvModule;
import api.database.MongoFactory;
import api.generated.CollectionFactory;

public class App {
    @Singleton
    @Component(modules = {
            DotenvModule.class,
            MongoFactory.class,
            CollectionFactory.class, // generated
            ApiFactory.class,
            TaskApiBuilder.class
    })
    public interface ApiApp {
        MongoDatabase database();

        MongoCollection<User> userCollection();

        TaskApiBuilder taskApiBuilder();

        Javalin javalin();
    }

    public String getGreeting() {
        return "Hello World";
    }

    public static void main(String[] args) {

        ApiApp api = DaggerApp_ApiApp
                .builder()
                .build();

        var app = api.javalin();
        api.taskApiBuilder().apply();
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