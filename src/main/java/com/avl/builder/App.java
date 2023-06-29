package com.avl.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.bson.Document;

import com.avl.builder.basic.PathTree;

public class App 
{
    public static void main( String[] args ) throws JsonProcessingException, FileNotFoundException
    {

        MongoClient mongoClient = MongoClients.create("mongodb://LOGIN:PASSWORD@HOST/");
        MongoDatabase database = mongoClient.getDatabase("bacteries");
        MongoCollection<Document> collection = database.getCollection("LSU_slim_filtered");

        PathTree pathTree = new PathTree();

        for (Document document : collection.find()) {
            String name = (String) ((Document) document.get("_id")).get("names");
            pathTree.InsertPath(name);
        }

        StringBuilder buffer = new StringBuilder();
        pathTree.subtable.print(buffer, "", "");
        try (PrintWriter out = new PrintWriter("result.txt")) {
            out.println(buffer.toString());
        }
        
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(pathTree.subtable);
        try (PrintWriter out = new PrintWriter("result.json")) {
            out.println(json);
        }
    }
}
