package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.GameCharacter;
import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PersonController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public User showRegistrationForm(Model model) {
        log.info("get register");
        //model.addAttribute("user", new User());
        return new User();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        log.info("post register");

        // Check if the username or email already exists
        if (userService.usernameExists(user.getUsername()) || userService.existsByEmail(user.getEmail())) {
            log.warn("User with the same username or email already exists");
            return new ResponseEntity<>("User with the same username or email already exists", HttpStatus.BAD_REQUEST);
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        userService.registerUser(user.getUsername(), hashedPassword, user.getEmail());

        // Find the user by username
        User registeredUser = userService.findByUsername(user.getUsername());
        log.info("User registered - Username: {}, Password: {}, UserExists: {}", registeredUser.getUsername(),
                registeredUser.getPassword(), registeredUser != null);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginPerson(@RequestBody Map<String, String> credentials) {

        log.info("login person");

        String username = credentials.get("username");
        String password = credentials.get("password");

        log.info("login post - Username: {}", username);

        // Authenticate the user
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Add logic for successful authentication, if needed

            return ResponseEntity.ok("User authenticated successfully!");
        } else {
            // Add logic for unsuccessful authentication
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
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