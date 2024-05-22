package com.ta.pocketRPG.controller;


import com.ta.pocketRPG.component.RequestRateLimiter;
import com.ta.pocketRPG.domain.dto.FightRequest;
import com.ta.pocketRPG.domain.dto.MyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ta.pocketRPG.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {
    private final UserService service;

    @Autowired
    private RequestRateLimiter rateLimiter;

    @GetMapping
    public ResponseEntity<?> example() {
        log.info("Example");


        List<MyRequest> listRequest = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MyRequest myRequest = new MyRequest();
            myRequest.setCharacterName("name "+i);
            myRequest.setCharacterHp(11+i);
            listRequest.add(myRequest);
        }
        return ResponseEntity.ok(listRequest);

//        if (rateLimiter.allowRequest()) {
//            // Process the request
//            return "Request processed successfully";
//        } else {
//            // Return an error response or handle the rate limit exceeded scenario
//            return "Rate limit exceeded. Please try again later.";
//        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }

    @GetMapping("/get-admin")
    public void getAdmin() {
        service.getAdmin();
    }
}