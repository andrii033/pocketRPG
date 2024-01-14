package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class PersonController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPerson(@ModelAttribute("user") User user) {
        log.info("post");
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        userService.registerUser(user.getUsername(),hashedPassword);

        return "redirect:/login";
    }


}