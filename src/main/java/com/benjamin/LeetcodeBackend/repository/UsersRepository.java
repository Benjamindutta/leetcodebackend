package com.benjamin.LeetcodeBackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.benjamin.LeetcodeBackend.collection.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
    @Query(value = "{ 'username' : ?0 }", fields = "{ 'questionsSolved': 1 }")
    User findByUserNameIncludeQuestionSolved(String username);
    @Query(value = "{'username':?0}",fields = "{'questionSolved':1}")
    User findByUserNameIncludeQuestionRevison(String username);
}
