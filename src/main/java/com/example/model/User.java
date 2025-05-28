package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
/*
@Entity //marks this class as a JPA entity to map to a database table
@Table(name="users") //map it to the users table in the database
 */
//this class represents the users structure in the database
//it integrates with spring security
@Entity //marks this class as a JPA entity to map to database table
@Table(name="users") //map the entity to the users table in the database
@Setter //lombok will automatically generate setters and getters for all fields
@Getter
public class User implements UserDetails { //implements UserDetails so spring security can use this class
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //auto generates unique user IDs
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expiration")
    private LocalDateTime verificationExpiration;

    @Column
    private boolean enabled; //user activation status (related to verification)

    public User(String username, String email, String password) { //for creating new users during registration
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {} //default constructor required by JPA
    //required methods from UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // we don't have roles/authorities
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; } //returns empty list because we are not handling account expiration

    @Override
    public boolean isAccountNonLocked() {
        return true; } // we are not handing account lock

    @Override
    public boolean isCredentialsNonExpired() {
        return true; } //we are not handling password expiration

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}