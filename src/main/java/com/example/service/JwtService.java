package com.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.function.Function;

/* JWT provides secure, stateless authentication for API endpoints, because it ensures only authorized users access protected resources.
when a user logs in, this service:
creates a JWT containing the username as the subject
adds a 1-hour expiration time
sign it using the secret key

when a user sends a request with that token:
extracts the username (claims) from it for authentication
check if the token is valid by checking the signature and expiration */

@Service //tells spring this class is a service used to handle business JWT-related logic, and it's automatically available to be used by other classes
public class JwtService {
    //these read values from application.properties
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /*
    converts the secret key from properties (it's Base64-encoded string) into a secure cryptography key object used for:
    Signing JWT tokens when generating them
    verifying JWT tokens when validating them
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
    Builds the token using user's claims, username, issuedAt, and expiration
    Uses HS256 algorithm and the signing key from getSignInKey()
    returns the final compact JWT string
     */
    private String buildToken(Map<String, Object> extraClaims, //if(any
                              UserDetails userDetails, //username as subject
                              long expiration){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){//allows passing extra data in the token (claims)
        return buildToken(extraClaims, userDetails, jwtExpiration); //overloaded method to call buildToken()
    }

    public String generateToken(UserDetails userDetails){//for standard tokens //simplifies token creation by calling buildToken() internally
        return generateToken(new HashMap<>(), userDetails);
    }

    private Claims extractAllClaims(String token){//decodes and parses the token body //get all claims, parses the token and extracts its body (the payload containing claims like username and expiration)
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){//extracts specific claim data from token (e.g., username, expiration)
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){//gets the subject (username) from token
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){//get the expiration date from token
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){//is token still valid time-wise using expiration
        return extractExpiration(token).before(new Date());//compares the expiration date to the current time
    }

    /*combines two token-checks:
    username matches the current user
    token is not expired */
    public boolean isTokenValid(String token, UserDetails userDetails){//validates token ownership and expiration
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    public long getExpirationTime()
    {
        return jwtExpiration;
    }
}
/*
[Spring Boot Starts]
      ↓
Reads @Value configs from application.properties
      ↓
JwtService bean created by Spring (@Service)
      ↓
Token Creation: generateToken() → buildToken() → getSignInKey()
      ↓
Token Extraction: extractClaim() → extractAllClaims() → getSignInKey()
      ↓
Token Validation: isTokenValid() → extractUsername() + isTokenExpired()
 */