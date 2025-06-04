package com.example.service;

import com.example.dto.LoginUserDTO;
import com.example.dto.RegisterUserDTO;
import com.example.dto.VerifyUserDTO;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

//handles signup, login, and verification using spring security
/*
- generate verification code: create a 6-digit random code
- send verification email: send a HTML mail with the generated code
- signup: create user, hashes password, generate verification code, send verification email
- authenticate: verifies user's credentials and check if the account is verified
- verifyUser: Handles email verification after signup
- re-send verification email: generate a new code for unverified users and send it in a HTML email
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository; //Save and fetch users
    private final PasswordEncoder passwordEncoder; //Encode passwords securely
    private final AuthenticationManager authenticationManager; //Authenticate login attempts
    private final EmailService emailService; //Send verification emails

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    //generates a 6-digits random code between 100000 and 999999
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    private void sendVerificationEmail(User user) {//sends a HTML-formatted verification email
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE" + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    this method handles new user registration:
    - takes the username, email, and password from the registrationDTO
    - encode the password securely
    - create a new user object
    - generate a 6-digit verification code and expiration time
    - sets user as not enabled (unverified)
    - sends a verification email with the code
    - saves the user to the database
     */

    public User signUp(RegisterUserDTO input) {
        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    /*
    authentication handles login process:
    - find user by email
    - if not found → throw error
    - if not verified → throw error
    - authenticate with spring security's AuthenticationManager
    - if authentication succeeds → return the user
     */
    public User authenticate(LoginUserDTO input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified, Please verify your account.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
        return user;
    }

    /*
    handles email verification after signup:
    if not found → throw error
    if code expired → throw error
    if code doesn't match → throw error
    if the code matches → enable the account and clear the code
     */
    public void verifyUser(VerifyUserDTO input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    /*
    allows a user to resend the verification code:
    - looks up user by email
    - if already verified → throw error
    - generate a new code and sets new expiration
    - sends email
    - saves user
     */
    public void resendVerificationCoe(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("User already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}