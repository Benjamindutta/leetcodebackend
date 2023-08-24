package com.benjamin.LeetcodeBackend.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.benjamin.LeetcodeBackend.collection.Question;
import com.benjamin.LeetcodeBackend.dto.QuestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.benjamin.LeetcodeBackend.collection.User;
import com.benjamin.LeetcodeBackend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	public List<User> users(){
		return userService.findAllUsers();
	}
	
	
	@PostMapping("")
	public ResponseEntity<User> createUser(@RequestBody User user){
		User responseUser =userService.save(user);
		return ResponseEntity.ok(responseUser);
	}
	@PostMapping("{userId}")
	public ResponseEntity<String> submitSolution(@RequestBody QuestionDto questionDto, @PathVariable String userId){
		Question question = new Question();

		question.setId(questionDto.getQuestionId());
		question.setUrl(questionDto.getUrl());
		question.setDifficulty(questionDto.getQuestionDifficulty());
		question.setTitle(questionDto.getQuestionTitleString());
		System.out.println(question);


		User user =userService.getUserByid(questionDto.getUserName()).orElse(new User());
//		System.out.println(user);
		if(user.getUsername()==null) {
			user.setUsername(questionDto.getUserName());
			Set<Question> solvedQuestions=new HashSet<>();
			Set<Question> questionsToRevised=new HashSet<>();
			solvedQuestions.add(question);
			user.setQuestionsSolved(solvedQuestions);
			user.setQuestionsToRevised(questionsToRevised);

		}else{
			Set<Question> solvedQuestions=user.getQuestionsSolved();
			Long count=solvedQuestions.stream().filter(q->q.getId().equals(question.getId())).count();
//			System.out.println(count);
			if(count>0){
				return ResponseEntity.ok("You have already submitted");
			}else{
				solvedQuestions.add(question);
				user.setQuestionsSolved(solvedQuestions);
			}

		}
		userService.save(user);
		return ResponseEntity.ok("Submitted successfully");
	}


}
