package com.example.dto;

import lombok.Getter;
import lombok.Setter;

//it holds registration data sent from frontend as a RegisterUserDTO instance
@Setter
@Getter
public class RegisterUserDTO {
    private String username;
    private String email;
    private String password;
}

/*
when a user submits a registration form like:
  "username": "john_doe",
  "email": "john@example.com",
  "password": "mypassword"
  spring will automatically bind that JSON input to an instance of RegisterUserDTO
 */