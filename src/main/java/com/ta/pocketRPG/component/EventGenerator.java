package com.ta.pocketRPG.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventGenerator {

    @Scheduled(fixedRate = 1000)
    public void generateEvent(){
        //log.info("Generating event every second...");
    }
}
