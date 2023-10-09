package com.benjamin.LeetcodeBackend.controller;


import com.benjamin.LeetcodeBackend.collection.Question;
import com.benjamin.LeetcodeBackend.collection.User;
import com.benjamin.LeetcodeBackend.dto.Difficulty;
import com.benjamin.LeetcodeBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/difficulty/")
public class DifficultyController {

    @Autowired
    private UserService userService;

    @GetMapping("{username}")
    public Difficulty getDiffQuestions(@PathVariable String username){
        User user = userService.getUserbyUsername(username);
        Set<Question> solvedQuestion=user.getQuestionsSolved();
        Set<Question> revisionQuesiton=user.getQuestionsToRevised();

        Set<Question> mergedSet = Stream.concat(solvedQuestion.stream(), revisionQuesiton.stream())
                .collect(Collectors.toSet());

        Long easy=  mergedSet.stream().filter(q->q.getDifficulty().equals("Easy")).count();
        Long medium=mergedSet.stream().filter(q->q.getDifficulty().equals("Medium")).count();
        Long hard=mergedSet.stream().filter(q->q.getDifficulty().equals("Hard")).count();

        Difficulty diff=new Difficulty();
        diff.setEasy(easy);
        diff.setHard(hard);
        diff.setMedium(medium);

        return diff;
    }

}
