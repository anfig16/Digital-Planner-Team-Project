package com.planner.digitalPlanner.Config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration // Marks this class as a configuration class
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final Dotenv dotenv = Dotenv.load(); // Loads the .env file to access environment variables

    // This method specifies the name of the MongoDB database to use
    @Override
    protected String getDatabaseName() {
        return dotenv.get("MONGO_DB"); // Retrieves the database name from the .env file
    }

    // This method defines how to create a MongoClient connection
    @Override
    public MongoClient mongoClient() {
        String uri = dotenv.get("MONGO_URI"); // Retrieves the MongoDB URI from the .env file
        return MongoClients.create(new ConnectionString(uri)); // Creates and returns a MongoClient
    }

    // Enables automatic index creation (useful for ensuring indexes are created on collections)
    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
