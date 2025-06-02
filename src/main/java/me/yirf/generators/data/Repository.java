package me.yirf.generators.data;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Ideally add a @Json(name = "_id) to the id field
 *
 * @param <K>
 * @param <V>
 */
public class Repository<K, V> {
    private final Moshi serializer;
    private final MongoCollection<Document> collection;
    private final Class<V> clazz;

    public Repository(
            Moshi serializer, MongoDatabase database, String collectionName, Class<V> clazz
    ) {
        this.serializer = serializer;
        this.collection = database.getCollection(collectionName, Document.class);
        this.clazz = clazz;
    }

    public V get(K key) {
        return get(Filters.eq("_id", key));
    }

    public V get(Bson filter) {
        Document document = collection.find(filter).first();
        if (document == null) {
            return null;
        }

        try {
            return serializer.adapter(clazz).fromJson(document.toJson());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // dont do uuids in here because it silently fails despite having a representation
    public List<V> query(Bson filter) {
//        Logger.info("Executing query: " + filter.toBsonDocument().toJson());
        return collection.find(filter).map(document -> {
            try {
                return Objects
                        .requireNonNull(serializer.adapter(clazz).fromJson(document.toJson()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).into(new ArrayList<>());
    }

    public void put(K key, V value) {
        collection.updateOne(
                Filters.eq("_id", key),
                new Document("$set", Document.parse(serializer.adapter(clazz).toJson(value))),
                new UpdateOptions().upsert(true)
        );
    }

    public List<V> values() {
        return collection.find().map(document -> {
            try {
                return Objects
                        .requireNonNull(serializer.adapter(clazz).fromJson(document.toJson()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).into(new ArrayList<>());
    }

    public void delete(K key) {
        collection.deleteOne(Filters.eq("_id", key));
    }
}