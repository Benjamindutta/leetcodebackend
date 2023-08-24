package com.benjamin.LeetcodeBackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    private Long  questionId;
    private String userName;
    private String questionTitleString;
    private String url;
    private String questionDifficulty;
}
