package com.benjamin.LeetcodeBackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.benjamin.LeetcodeBackend.collection.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
    @Query(value = "{ 'username' : ?0 }", fields = "{ 'questionsSolved': 1 }")
    Optional<User> findByUserNameIncludeQuestionSolved(String username);
    @Query(value = "{'username':?0}",fields = "{'questionsToRevised':1}")
    Optional<User> findByUserNameIncludeQuestionRevison(String username);
    @Query(value="{'leetcodeUsername':?0}")
    Optional<User> findByLeetcodeUsername(String leetcodeUsername);

    User findByUsername(String username);
}
