package com.benjamin.LeetcodeBackend.controller;

import com.benjamin.LeetcodeBackend.dto.chatGPTMessagePrompt;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/chatgpt/")
public class ChatGPTController {
    @Value("${gpt.my.api}")
    String gptkey;

    @PostMapping("/chat")
    public ResponseEntity<?> getChatMessages(@RequestBody chatGPTMessagePrompt prompt) {

        try{
            OpenAiService service = new OpenAiService(gptkey);
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder().messages(prompt.getPromptMessages())
                    .model("gpt-3.5-turbo-16k").build();
            String results= service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
            return ResponseEntity.ok(results);
        }catch (Exception e){
            return ResponseEntity.ok("some Error occured");
        }
    }
}
