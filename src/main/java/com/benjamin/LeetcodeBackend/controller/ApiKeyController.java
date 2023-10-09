package com.benjamin.LeetcodeBackend.controller;


import com.benjamin.LeetcodeBackend.dto.AwsSecrets;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@RestController
@RequestMapping("/api/key")
public class ApiKeyController {

    private final static Gson gson=new Gson();

//    @Value("${gpt.my.api}")
//    private String openaikey;
//    @Value("${youtube.my.key}")
//    private String youtube_key;

    @GetMapping("/youtube")
    public String getKey(){
        return getSecret().getYoutube_key();
    }

    @GetMapping("/openai")
    public String getOpenaikey(){
        return getSecret().getChatgptkey();
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
