package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.Enemy;
import com.ta.pocketRPG.model.GameCharacter;
import com.ta.pocketRPG.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FightController {
    @Autowired
    private UserService userService;
    @GetMapping("/fight")
    public String fightGet(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        GameCharacter gameCharacter = userService.loadGameCharacterByUsername(username);
        model.addAttribute("enemy",new Enemy());
        model.addAttribute("gameCharacter",gameCharacter);
        return "fight";
    }
}
