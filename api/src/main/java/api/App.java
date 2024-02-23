package api;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import api.database.entities.User;
import api.env.EnvConfig;
import api.config.MongoConfig;
import api.database.entities.AuthProvider;

public class App {
    public String getGreeting() {
        return "Hello World";
    }

    public static void main(String[] args) {
        var env = Dotenv
                .configure()
                .systemProperties()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        var mongoConfig = new EnvConfig<MongoConfig>(MongoConfig.class, new MongoConfig(), "database")
                .FromEnv(env)
                .BuildWithPublicFields();

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        var app = Javalin
                .create()
                .start(7070);

        try (MongoClient mongoClient = MongoClients.create(mongoConfig.ToConnectionString())) {
            MongoDatabase database = mongoClient.getDatabase("zapcode").withCodecRegistry(pojoCodecRegistry);
            MongoCollection<User> collection = database.getCollection("users", User.class);
            User user = collection.find(eq("email", "test@testing.testing")).first();

            if (user == null) {
                user = new User();
                user.id = ObjectId.get();
                user.authProvider = AuthProvider.GITHUB;
                user.email = "test@testing.testing";

                var res = collection.insertOne(user);
                res.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        app.get("/users/<email>", ctx -> {
            try (MongoClient mongoClient = MongoClients.create(mongoConfig.ToConnectionString())) {
                MongoDatabase database = mongoClient.getDatabase("zapcode").withCodecRegistry(pojoCodecRegistry);
                MongoCollection<User> collection = database.getCollection("users", User.class);
                User foundUser = collection.find(eq("email", ctx.pathParam("email"))).first();
                ctx.json(foundUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        app.get("/", ctx -> ctx.result("Hello World"));
        app.get("/<name>", ctx -> ctx.result("Hello " + ctx.pathParam("name")));
    }
}