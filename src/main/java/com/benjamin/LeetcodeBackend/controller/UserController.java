package com.benjamin.LeetcodeBackend.controller;

import java.util.*;

import com.benjamin.LeetcodeBackend.collection.Question;
import com.benjamin.LeetcodeBackend.dto.QuestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.benjamin.LeetcodeBackend.collection.User;
import com.benjamin.LeetcodeBackend.service.UserService;

@RestController
@RequestMapping("/api/users/")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	public List<User> users(){
		return userService.findAllUsers();
	}
	@GetMapping("isUser/{userId}")
	public boolean isUser(@PathVariable String userId){
		try{
			User user =userService.getUserByid(userId).orElse(new User());
			if(user.getUsername().equals(null)){
				return false;
			}
			return true;
		}catch (Exception e){
			return false;
		}
	}
	@GetMapping("solvedquestions/{username}")
	public ResponseEntity<?> getSolvedQuestion(@PathVariable String username){
		try{
			Set<Question> questionsSolved=userService.getAllSolvedQuestionByUserName(username);
			return ResponseEntity.ok(questionsSolved);
		}catch(Exception e){
			return ResponseEntity.ok("No such user available!!");
		}
	}
	@GetMapping("revisionquestions/{username}")
	public ResponseEntity<?> getRevisionQuestions(@PathVariable String username){
		try{
			Set<Question> revisionQuestion= userService.getAllRevisionQuestionByUser(username);
			return ResponseEntity.ok(revisionQuestion);
		}catch(Exception e){
			return ResponseEntity.ok("No such user available!!");
		}
	}
	
	@PostMapping("")
	public ResponseEntity<?> createUser(@RequestBody User user){
		try{
			User responseUser =userService.save(user);
			return ResponseEntity.ok(responseUser);
		}catch(Exception e){
			return ResponseEntity.ok(e.getMessage());
		}

	}
	@PostMapping("addrevisedquestion/{userId}")
	public ResponseEntity<?> addRevisedQuestion(@PathVariable String userId,@RequestBody Question question){
		try{
			User user=userService.getUserByid(userId).orElse(new User());
			if(user.getUsername()==null){

				return ResponseEntity.ok("user Not found!!!!!!!!");
			}else{
				Set<Question> newRevisedQuestions=user.getQuestionsToRevised();
				newRevisedQuestions.add(question);
				Set<Question> questionSolved = user.getQuestionsSolved();
				questionSolved.remove(question);
				user.setQuestionsToRevised(newRevisedQuestions);
				user.setQuestionsSolved(questionSolved);
				return ResponseEntity.ok(userService.save(user));
			}
		}catch(Exception e){
			return ResponseEntity.ok("user not found!!!!!");
		}
	}
	@PostMapping("{userId}")
	public ResponseEntity<String> submitSolution(@RequestBody QuestionDto questionDto, @PathVariable String userId){
		Question question = new Question();

		question.setId(questionDto.getQuestionId());
		question.setUrl(questionDto.getUrl());
		question.setDifficulty(questionDto.getQuestionDifficulty());
		question.setTitle(questionDto.getQuestionTitleString());
		System.out.println(question);


		try{
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

		}catch (Exception e){
			return ResponseEntity.ok("Some error occured");
		}
	}


}
