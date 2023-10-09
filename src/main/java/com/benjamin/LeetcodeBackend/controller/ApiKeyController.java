package com.benjamin.LeetcodeBackend.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/key")
public class ApiKeyController {

    @Value("${gpt.my.api}")
    private String openaikey;
    @Value("${youtube.my.key}")
    private String youtube_key;

    @GetMapping("/youtube")
    public String getKey(){
        return youtube_key;
    }

    @GetMapping("/openai")
    public String getOpenaikey(){
        return openaikey;
    }

}
