package com.example.dto;

import lombok.Getter;
import lombok.Setter;

//this holds the login credentials when a user login as LoginUserDTO instance, so then the authentication can validate and generate a JWT if credentials are correct
@Setter
@Getter
public class LoginUserDTO {
    private String username;
    private String password; //will later be checked and hashed by spring security
}
/*
when a user submits a login form like:
json
{
  "email": "user@example.com",
  "password": "secret123"
}
spring will bind this JSON input into LoginUserDTO instance
 */