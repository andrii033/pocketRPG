package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.GameCharacter;
import com.ta.pocketRPG.model.User;
import com.ta.pocketRPG.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
public class ChooseCharacter {
    @Autowired
    private UserService userService;

    @GetMapping("/chooseCharacter")
    public String chooseCharacterForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<GameCharacter> userCharacters = userService.getAllGameCharactersByUsername(username);
        model.addAttribute("userCharacters",userCharacters);
        return "chooseCharacter";
    }

    @PostMapping("/chooseCharacter")
    public String chooseCharacter(@ModelAttribute("existingCharacter") Long selectedCharacterId) {
        log.info("id " + selectedCharacterId);

        // Assuming you have access to the currently logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Retrieve the existing user
        User user = userService.findByUsername(username);

        // Update the chosenCharacterId in the existing user
        user.setChosenCharacterId(selectedCharacterId);

        // Save the changes to the user in the database
        userService.updateUser(user);

        return "redirect:/fight";
    }

}
