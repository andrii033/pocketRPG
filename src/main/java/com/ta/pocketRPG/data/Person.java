package com.ta.pocketRPG.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 15)
    private String name;
    @NotBlank
    @Size(min = 6, max = 30)
    private String password;
    @Transient
    private String repassword;
    @NotBlank
    @Email
    private String email;

}
