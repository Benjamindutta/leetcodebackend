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
		SpringApplication.run(LeetcodeBackendApplication.class, args);
	}


}

