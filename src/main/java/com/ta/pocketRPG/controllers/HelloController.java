package com.ta.pocketRPG.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint. Anyone can access it!";
    }

    @GetMapping("/secured")
    public String securedEndpoint() {
        return "This is a secured endpoint. Only authenticated users can access it!";
    }
}
