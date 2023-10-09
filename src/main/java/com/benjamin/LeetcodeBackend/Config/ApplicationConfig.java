package com.benjamin.LeetcodeBackend.Config;


import com.benjamin.LeetcodeBackend.dto.AwsSecrets;
import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;


@Configuration
public class ApplicationConfig {

    private static Gson gson=new Gson();

//
//    @Autowired
////    private ConfigurableEnvironment environment;

//    @Bean
//    public void customizeProperties() {
//
//
////        MutablePropertySources propertySources = environment.getPropertySources();
////        AwsSecrets a=getSecret();
////        System.setProperty("gpt.my.api", a.getChatgptkey());
////        System.setProperty("youtube.my.key",a.getYoutube_key());
////        // Define your custom properties programmatically
////        Properties customProperties = new Properties();
////        customProperties.setProperty("gpt.my.api", a.getChatgptkey());
////        customProperties.setProperty("youtube.my.key",a.getYoutube_key());
////
////        // Add your custom properties to the environment
////        PropertiesPropertySource propertySource = new PropertiesPropertySource("customProperties", customProperties);
////        propertySources.addFirst(propertySource);
//    }

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

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.githubClientRegistration());
    }

    public ClientRegistration githubClientRegistration() {
        AwsSecrets awsSecrets=getSecret();
        return ClientRegistration.withRegistrationId("github")
                .clientId(awsSecrets.getGithubClientId())
                .clientSecret(awsSecrets.getGithubClientSecret())
                .clientName("GitHub")
                .scope("user:email,read:org")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("id")
                .build();

//        return ClientRegistration.withRegistrationId("google")
//                .clientId("google-client-id")
//                .clientSecret("google-client-secret")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//                .scope("openid", "profile", "email", "address", "phone")
//                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
//                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
//                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//                .clientName("Google")
//                .build();
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
