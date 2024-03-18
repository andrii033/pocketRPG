package com.ta.pocketRPG.component;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;

@Component
public class RequestRateLimiter {
    // Configure the rate limit (e.g., 1 request per second)
    private final RateLimiter rateLimiter = RateLimiter.create(1); // 1 request per second

    // Method to check if a request is allowed
    public boolean allowRequest() {
        return rateLimiter.tryAcquire(); // Returns true if the request is allowed
    }
}