package com.ta.pocketRPG.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignInRequest {

    @Size(min = 4, max = 50, message = "The user name must contain between 5 and 50 characters")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Size(min = 8, max = 255, message = "The length of the password should be from 8 to 255 characters")
    @NotBlank(message = "The password cannot be empty")
    private String password;
}