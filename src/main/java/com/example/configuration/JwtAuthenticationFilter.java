package com.example.configuration;

import com.example.service.JwtService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/* JSON Web Token is a small peace of text that proves you are logged in
it runs before the controller handles the request to check if the request includes a JWT token
if yes, it validates the token and authenticates the user
if not or it's invalid the request is not authenticated
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{ //means run this class once for every request before hitting the controller, (it acts as a gate-keeper before the backend work does anything)
    private final HandlerExceptionResolver handlerExceptionResolver; //if sm goes wrong (like invalid token) it will handle the error instead of crashing
    private final JwtService jwtService; //extract and validates the JWT
    private final UserDetailsService userDetailsService; //load the user from database by email

    public JwtAuthenticationFilter(HandlerExceptionResolver handlerExceptionResolver, JwtService jwtService, UserDetailsService userDetailsService) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
        this.userDetailsService=userDetailsService;
    }

    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException{
//is request doesn't have 'authorization' header or a JWT token ?
        //the request pass through without authentication (skips filtering)
        //if the request has both:
            //extract token then extract JWT and email from it using JWTService
            //check if the user is authenticated:
                //if yes: we don’t need to do anything
                //if not: load the user from database to validate the token using 'JwtService'
                    //if the token is valid: create and set authentication
                //sends the request forward to the controller
        // Catch and handle any errors


        //check the authorization header
        final String authHeader =request.getHeader("Authorization");

        //check if it has authorization header and JWT token
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return; //the request pass through without authentication (skips filtering)
        }
        try{
            //extract token and email
            final String jwt =  authHeader.substring(7);
            final String userEmail=jwtService.extractUsername(jwt);
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication(); //Check if the user is already authenticated if yes we don’t need to do anything
            //If user is not authenticated, validate the token
            if(userEmail!=null&&authentication==null){
                UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);
                //check if JWT is valid
                if(jwtService.isTokenValid(jwt, userDetails)){
                    //if it's valid create and set authentication
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //Tells spring security (this user is now authenticated and logged in)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken); //sets the authenticated user in the security context for protected endpoints
                }
            }
            //Let the request move on to the next part
            filterChain.doFilter(request, response); //This sends the request forward to the controller with the user now authenticated.
        } catch(Exception exception) { //Catch and handle any errors
            handlerExceptionResolver.resolveException(request, response, null, exception); //exception handling to route any exception to a global handler (instead of crashing the request)
            //If anything fails (e.g. token is bad), we don’t crash.
            //We pass the error to a custom exception handler.
        }
    }
}