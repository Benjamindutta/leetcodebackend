package com.benjamin.LeetcodeBackend.Config;


import com.benjamin.LeetcodeBackend.dto.AwsSecrets;
import com.google.gson.Gson;
import com.mongodb.ClientSessionOptions;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterDescription;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.List;
import java.util.Properties;

@Configuration
public class ApplicationConfig {

    private static Gson gson=new Gson();

//
//    @Autowired
////    private ConfigurableEnvironment environment;

    @Bean
    public void customizeProperties() {


//        MutablePropertySources propertySources = environment.getPropertySources();
        AwsSecrets a=getSecret();
        System.setProperty("gpt.my.api", a.getChatgptkey());
        System.setProperty("youtube.my.key",a.getYoutube_key());
//        // Define your custom properties programmatically
//        Properties customProperties = new Properties();
//        customProperties.setProperty("gpt.my.api", a.getChatgptkey());
//        customProperties.setProperty("youtube.my.key",a.getYoutube_key());
//
//        // Add your custom properties to the environment
//        PropertiesPropertySource propertySource = new PropertiesPropertySource("customProperties", customProperties);
//        propertySources.addFirst(propertySource);
    }

    @Bean
    public MongoClient mongoConfig(){
        AwsSecrets a=getSecret();
        ConnectionString connectionString = new ConnectionString(a.getMongourl());

        // Configure MongoClient settings
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        // Create and return the MongoClient
        return MongoClients.create(settings);

    }


    private static AwsSecrets getSecret() {

        String secretName = "prod/leetcodetracker/secrets";
        Region region = Region.of("ap-south-1");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            // For a list of exceptions thrown, see
            // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
            throw e;
        }

        String secret = getSecretValueResponse.secretString();

        // Your code goes here.
        return gson.fromJson(secret, AwsSecrets.class);
    }
}
