package Database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import crud.Server;
import io.micronaut.validation.Validated;
import org.bson.Document;

import javax.inject.Singleton;

@Singleton
@Validated
public class Connection {

    private final MongoClient mongoClient;

    public Connection(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoCollection<Server> getCollection() {
        return mongoClient
                .getDatabase("micronaut")
                .getCollection("servers", Server.class);
    }

    public com.mongodb.client.MongoCollection<Document> getConn(){
        com.mongodb.client.MongoClient mongoClient1 = MongoClients.create("mongodb://mongodb:27017");
        MongoDatabase database =  mongoClient1.getDatabase("micronaut");
        com.mongodb.client.MongoCollection<Document> coll = database.getCollection("servers");

        return coll;
    }
}
