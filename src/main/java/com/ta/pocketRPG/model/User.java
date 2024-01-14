package com.ta.pocketRPG.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String username;


    private String password;

    @Transient
    private String repassword;
//
//    @NotBlank
//    @Email
    private String email;
}
