package me.yirf.generators.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Mongo {

    private MongoDatabase database;
    private final String MONGO_URI;
    private final String DB_NAME;

    public Mongo(String uri, String name) {
        this.MONGO_URI = uri;
        this.DB_NAME = name;
    }

    public void connect() {
        CodecRegistry codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(MONGO_URI))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(codecRegistry).build());
        database = mongoClient.getDatabase(DB_NAME);
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}