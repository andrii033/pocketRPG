package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.GameCharacter;
import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PersonController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public User showRegistrationForm(Model model) {
        //model.addAttribute("user", new User());
        return new User();
    }


    //    @PostMapping("/register")
//    public String registerPerson(@ModelAttribute("user") User user) {
//        log.info("post");
//        String hashedPassword = passwordEncoder.encode(user.getPassword());
//        userService.registerUser(user.getUsername(), hashedPassword,user.getEmail());
//
//        // Find the user by username
//        User registeredUser = userService.findByUsername("user");
//        log.info("User registered - Username: {}, Password: {}, UserExists: {}", registeredUser.getUsername(),
//                registeredUser.getPassword(), registeredUser != null);
//
//        return "redirect:/login";
//    }
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        log.info("post");
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        userService.registerUser(user.getUsername(), hashedPassword, user.getEmail());

        // Find the user by username
        User registeredUser = userService.findByUsername("user");
        log.info("User registered - Username: {}, Password: {}, UserExists: {}", registeredUser.getUsername(),
                registeredUser.getPassword(), registeredUser != null);
        return "User registered successfully!";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginPerson(@RequestParam("username") String username, @RequestParam("password") String password) {
        log.info("login post - Username: {}, Password: {}", username, password);

        // Authenticate the user
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Add logic for successful authentication, if needed

            return "redirect:/createCharacter";
        } else {
            // Add logic for unsuccessful authentication, e.g., redirect to login page with an error message
            return "redirect:/login?error";
        }
    }

    @GetMapping("/createCharacter")
    public String createCharacterForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<GameCharacter> userCharacters = userService.getAllGameCharactersByUsername(username);

        model.addAttribute("userCharacters", userCharacters);
        model.addAttribute("gameCharacter", new GameCharacter());
        log.info("create character get");
        return "createCharacter";
    }

    @PostMapping("/createCharacter")
    public String createCharacter(@ModelAttribute("gameCharacter") GameCharacter gameCharacter,
                                  @ModelAttribute("user") User user) {
        log.info("post " + gameCharacter.getCharacterName());

        // Assuming you have access to the currently logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (gameCharacter.getStr() + gameCharacter.getAgi() + gameCharacter.getInte() > 8)
            return "redirect:/createCharacter";

        user = userService.findByUsername(username);

        // Create a new instance of GameCharacter
        GameCharacter newCharacter = new GameCharacter();
        newCharacter.setCharacterName(gameCharacter.getCharacterName());
        newCharacter.setStr(gameCharacter.getStr());
        newCharacter.setAgi(gameCharacter.getAgi());
        newCharacter.setInte(gameCharacter.getInte());

        // Save the new character
        userService.saveGameCharacter(username, newCharacter);

        return "redirect:/fight";
    }

}