package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.GameCharacter;
import com.ta.pocketRPG.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
    public String chooseCharacter(@ModelAttribute("gameCharacter") GameCharacter gameCharacter){

        return "redirect:/fight";
    }
}
