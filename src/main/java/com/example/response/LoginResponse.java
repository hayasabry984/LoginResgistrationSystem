package com.example.response;

import lombok.Getter;
import lombok.Setter;

//returns JWT and expiration time after a successful login for security
@Setter
@Getter
public class LoginResponse {
    private String token; //used for authenticating future requests
    private long expiresIn; //lets the frontend knows when the token will expire, so it can handle the logout or refresh

    public LoginResponse(String token, long expiresIn) {//constructor allows easy creation of this object to send in the HTTP response body after login
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
/*
this class is used in the response body when a user logs in:
response example: json{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600000
  }
*/