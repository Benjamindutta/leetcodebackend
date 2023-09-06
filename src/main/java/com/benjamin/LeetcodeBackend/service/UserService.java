package com.benjamin.LeetcodeBackend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.benjamin.LeetcodeBackend.collection.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.benjamin.LeetcodeBackend.collection.User;
import com.benjamin.LeetcodeBackend.repository.UsersRepository;

@Service
public class UserService {
	@Autowired
	private UsersRepository userrepo;

	public List<User> findAllUsers() {
		return userrepo.findAll();
	}

	public User save(User user) {
		
		return userrepo.save(user);
		
	}

	public Optional<User> getUserByid(String id){
		return userrepo.findById(id);
	}
	public Set<Question> getAllSolvedQuestionByUserName(String username){
		return userrepo.findByUserNameIncludeQuestionSolved(username).getQuestionsSolved();
	}

	public Set<Question> getAllRevisionQuestionByUser(String username) {
		return userrepo.findByUserNameIncludeQuestionRevison(username).getQuestionsToRevised();
	}
}
