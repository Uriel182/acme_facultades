/*
package com.legosoft.facultades.config;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.MongoFactory;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database}")
    private String mongodbName;

    @Bean
    Serializer axonJsonSerializer(){
        return new JacksonSerializer();
    }

    @Bean
    public EventStorageEngine eventStore(){
        return new MongoEventStorageEngine(
                axonJsonSerializer(),null, axonMongoTemplate(), new DocumentPerEventStorageStrategy());
    }

    @Bean
    public MongoTemplate axonMongoTemplate(){
        return new DefaultMongoTemplate(mongoClient(),mongodbName);
    }

    @Bean
    public MongoClient mongoClient(){
        MongoFactory mongoFactory = new MongoFactory();
        mongoFactory.setMongoAddresses(Collections.singletonList(new ServerAddress(mongoHost,mongoPort)));
        return mongoFactory.createMongo();
    }
}
*/
