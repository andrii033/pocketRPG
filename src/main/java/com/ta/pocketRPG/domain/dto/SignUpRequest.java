package com.ta.pocketRPG.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Size(min = 5, max = 50, message = "The user name must contain between 5 and 50 characters")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Size(min = 5, max = 255, message = "E-mail address must contain between 5 and 255 characters")
    @NotBlank(message = "E-mail address cannot be blank")
    @Email(message = "The email address must be in the format user@example.com")
    private String email;

    @Size(max = 255, message = "Password length must be no more than 255 characters")
    private String password;
}