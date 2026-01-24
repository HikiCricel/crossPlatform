package com.example.crossPlatform.bot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.crossPlatform.service.BotService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HealthCheck {
    private final BotService botService;

    @Scheduled(cron = "0 20 22 * * *")
    public void sendHeathCheck() {
        botService.sendToAdmin("I'm still alive");
    }
}
