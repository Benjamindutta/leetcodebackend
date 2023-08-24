package com.benjamin.LeetcodeBackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.benjamin.LeetcodeBackend.collection.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {

}
