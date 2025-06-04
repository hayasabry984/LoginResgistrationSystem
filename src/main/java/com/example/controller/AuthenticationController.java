package com.example.controller;

//a Spring Boot REST controller that handles user authentication-related API endpoints

import com.example.dto.LoginUserDTO;
import com.example.dto.RegisterUserDTO;
import com.example.dto.VerifyUserDTO;
import com.example.model.User;
import com.example.response.LoginResponse;
import com.example.service.AuthenticationService;
import com.example.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth") //Base URL, all endpoint are unser this
@RestController
public class AuthenticationController { //this rest controller handles user authentication-related API endpoints
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDTO){
        User registeredUser=authenticationService.signUp(registerUserDTO); //Calls authenticationService.signup() with user input.
        return ResponseEntity.ok(registeredUser); //Returns the registered user as the response.
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDTO){
        User authenticatedUser=authenticationService.authenticate(loginUserDTO);
        String jwtToken=jwtService.generateToken(authenticatedUser); //Generates a JWT token using JwtService.
        LoginResponse loginResponse=new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);//Returns the token and expiration in a LoginResponse.
    }

    @PostMapping("/verify")
    public ResponseEntity<?>verifyUser(@RequestBody VerifyUserDTO verifyUserDTO){
        try{
            authenticationService.verifyUser(verifyUserDTO);
            return ResponseEntity.ok("Account verified successfully");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());//Returns success or error message.
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try{
            authenticationService.resendVerificationCoe(email);
            return ResponseEntity.ok("Verification code resent");
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());//Returns success or error message.
        }
    }
}