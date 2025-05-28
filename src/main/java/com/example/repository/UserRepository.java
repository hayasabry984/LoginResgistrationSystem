package com.example.repository;

import com.example.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

//this interface handles database operation on the user entity
// it uses CRUD operations as spring data JPA generated database queries automatically simplifying CRUD operations

public interface UserRepository extends CrudRepository<User,Long> {
    //optional handles cases while user doesn't exist (handles null safely, avoiding NullPointerException)
    Optional<User> findByEmail(String email); //returns user

    Optional<User> findByVerificationCode(String VerificationCode);

    //spring handles the query logic, so manual SQL needed
}