package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService { //handles logic related to users, it retrieves all users in the database
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers(){
        List<User> users=new ArrayList();
        userRepository.findAll().forEach(users::add);
        return users;
    }
}