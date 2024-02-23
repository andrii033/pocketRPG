package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/public")
    public String publicEndpoint(User user) {
        userService.usernameExists("user");
        log.info("User registered - Username: {}, Password: {}, UserExists: {}",
                user.getUsername(),user.getPassword(),user.getId());
        return "public";
    }

    @PostMapping("/secured")
    public ResponseEntity<String> securedEndpoint() {
        return new ResponseEntity<>("secured", HttpStatus.OK);
    }

}
