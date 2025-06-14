package com.example.dto;

import lombok.Getter;
import lombok.Setter;

//holds verification data sent from frontend, before enabling the account, as VerifyUserDTO instance
@Getter
@Setter
public class VerifyUserDTO {
    @Getter
    private String email;
    private String verificationCode;
}
/*
when a user receives a verification code in their email and submits it via a form like:
json
{
  "email": "jane@example.com",
  "verificationCode": "123456"
}
spring binds this input into a VerifyUserDTO instance so the backend can check if the code is correct and valid
 */