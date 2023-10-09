package com.benjamin.LeetcodeBackend;

import com.benjamin.LeetcodeBackend.dto.AwsSecrets;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@SpringBootApplication
public class LeetcodeBackendApplication {
	private static Gson gson=new Gson();
	public static void main(String[] args) {
		setEnvironmentVariables();
		SpringApplication.run(LeetcodeBackendApplication.class, args);
	}

	private static void setEnvironmentVariables() {
		AwsSecrets a=getSecret();
		System.setProperty("gpt.my.api", a.getChatgptkey());
		System.setProperty("youtube.my.key",a.getYoutube_key());
		System.setProperty("spring.security.oauth2.client.registration.github.clientId",a.getGithubClientId());
		System.setProperty("spring.security.oauth2.client.registration.github.clientSecret",a.getGithubClientSecret());
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

