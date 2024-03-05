package api.database;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import api.config.MongoConfig;
import dagger.Module;
import dagger.Provides;

@Module
public class MongoFactory {
  private static String DATABASE = "zapcode"; // TODO - read from MongoConfig

  private CodecProvider pojoCodecProvider;
  private CodecRegistry pojoCodecRegistry;
  private MongoClient client;
  private MongoDatabase database;

  public MongoFactory() {
    this.pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    this.pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
  }

  @Inject
  @Provides
  @Singleton
  public MongoDatabase provideDatabase(MongoConfig config) {
    if (this.database == null) {
      this.client = MongoClients.create(config.ToConnectionString());
      this.database = this.client.getDatabase(DATABASE).withCodecRegistry(pojoCodecRegistry);
    }
    return this.database;
  }
}
