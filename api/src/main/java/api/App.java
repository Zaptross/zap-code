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
import api.config.DotenvModule;
import api.database.MongoFactory;

public class App {
    @Singleton
    @Component(modules = {
            DotenvModule.class,
            MongoFactory.class,
    })
    public interface ApiApp {
        MongoDatabase database();
    }

    public String getGreeting() {
        return "Hello World";
    }

    public static void main(String[] args) {
        ApiApp api = DaggerApp_ApiApp
                .builder()
                .build();

        var app = Javalin
                .create()
                .start(7070);

        try {
            MongoDatabase database = api.database();
            MongoCollection<User> collection = database.getCollection("users", User.class);
            User user = collection.find(eq("email", "test@testing.testing")).first();

            if (user == null) {
                user = new User();
                user.id = ObjectId.get();
                user.authProvider = AuthProvider.GITHUB;
                user.email = "test@testing.testing";

                res.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        app.before("/users/<email>", new AuthHandler());
        app.get("/users/<email>", ctx -> {
            try {
                MongoDatabase database = api.database();
                MongoCollection<User> collection = database.getCollection("users", User.class);
                User foundUser = collection.find(eq("email", ctx.pathParam("email"))).first();
                ctx.json(foundUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        app.get("/<name>", ctx -> ctx.result("Hello " + ctx.pathParam("name")));
    }
}