package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {//handles user-related HTTP requests

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User>authenticatedUser(){ //Get current logged-in user
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User currentUser=(User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers(){//	Get list of all users
        List<User> users=userService.allUsers();
        return ResponseEntity.ok(users);
    }
}