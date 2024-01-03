package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PersonController {

    @Autowired
    private UserService userService; // Inject the UserService

    // other methods...

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("get register");
        userService.registerUser("user","user");
        return "register";
    }

    @PostMapping("/register")
    public String registerPerson(User user) {
        System.out.println("post");
        return "register";
    }

}