package com.ta.pocketRPG.controllers;

import com.ta.pocketRPG.model.Enemy;
import com.ta.pocketRPG.model.GameCharacter;
import com.ta.pocketRPG.services.EnemyService;
import com.ta.pocketRPG.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class FightController {
    @Autowired
    private UserService userService;
    @Autowired
    private EnemyService enemyService;

    private Enemy enemy=new Enemy();
    @GetMapping("/fight")
    public String fightGet(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        GameCharacter gameCharacter = userService.loadGameCharacterByUsername(username);
        model.addAttribute("enemy",enemy);
        model.addAttribute("gameCharacter",gameCharacter);
        return "fight";
    }

    @PostMapping("/fight")
    public String fightPost(@ModelAttribute("gameCharacter") GameCharacter gameCharacter,@ModelAttribute("enemy") Enemy enemy, BindingResult bindingResult){
        log.info("post fight");
        log.info(bindingResult.toString());
        enemy.setHp(5);
        return "fight";
    }
}
