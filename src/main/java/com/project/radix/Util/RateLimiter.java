package com.project.radix.Util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SECONDS = 60;
    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    public boolean isAllowed(String key) {
        cleanExpired();
        Attempt a = attempts.computeIfAbsent(key, k -> new Attempt());
        synchronized (a) {
            long now = Instant.now().getEpochSecond();
            if (now - a.windowStart > WINDOW_SECONDS) {
                a.count = 1;
                a.windowStart = now;
                return true;
            }
            if (a.count >= MAX_ATTEMPTS) {
                return false;
            }
            a.count++;
            return true;
        }
    }

    public long secondsUntilReset(String key) {
        Attempt a = attempts.get(key);
        if (a == null) return 0;
        long elapsed = Instant.now().getEpochSecond() - a.windowStart;
        return Math.max(0, WINDOW_SECONDS - elapsed);
    }

    private void cleanExpired() {
        long now = Instant.now().getEpochSecond();
        attempts.entrySet().removeIf(e -> now - e.getValue().windowStart > WINDOW_SECONDS + 10);
    }

    private static class Attempt {
        int count;
        long windowStart = Instant.now().getEpochSecond();
    }
}
