package com.benjamin.LeetcodeBackend.dto;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public class chatGPTMessagePrompt {
    private List<ChatMessage> promptMessages;

    public List<ChatMessage> getPromptMessages() {
        return promptMessages;
    }

    public void setPromptMessages(List<ChatMessage> promptMessages) {
        this.promptMessages = promptMessages;
    }
}
