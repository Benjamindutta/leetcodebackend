package com.benjamin.LeetcodeBackend.dto;


import lombok.Data;

@Data
public class AwsSecrets {
    private String chatgptkey;
    private String mongourl;
    private String youtube_key;
    private String githubClientId;
    private String githubClientSecret;
}
