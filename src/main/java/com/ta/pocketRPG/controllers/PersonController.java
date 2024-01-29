package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.GameCharacter;
import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPerson(@ModelAttribute("user") User user) {
        log.info("post");
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        userService.registerUser(user.getUsername(), hashedPassword,user.getEmail());

        // Find the user by username
        User registeredUser = userService.findByUsername("user");
        log.info("User registered - Username: {}, Password: {}, UserExists: {}", registeredUser.getUsername(), registeredUser.getPassword(), registeredUser != null);

//        GameCharacter gameCharacter = new GameCharacter();
//        gameCharacter.setCharacterName("Default Character");
//        gameCharacter.setLvl(1);
//
//        userService.saveGameCharacter(user.getUsername(), gameCharacter.getCharacterName());

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        log.info("login get");
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
        model.addAttribute("gameCharacter", new GameCharacter());
        log.info("create character get");
        return "createCharacter";
    }

    @PostMapping("/createCharacter")
    public String createCharacter(@ModelAttribute("gameCharacter") GameCharacter gameCharacter) {
        log.info("post "+gameCharacter.getCharacterName());

        // Assuming you have access to the currently logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(gameCharacter.getStr()+ gameCharacter.getAgi()+ gameCharacter.getInte() > 8)
            return "redirect:/createCharacter";
        userService.saveGameCharacter(username, gameCharacter);

        return "redirect:/fight";
    }

}