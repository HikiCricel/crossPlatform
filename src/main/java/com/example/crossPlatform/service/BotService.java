package com.example.crossPlatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class BotService extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(BotService.class);
    private final String botName;
    private final String usrId;

    public BotService(
            @Value("${telegram.bot.name}") String botName,
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.chat-id}") String usrId) {
        super(botToken);
        this.botName = botName;
        this.usrId = usrId;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            logger.info("Received message: {}", update.getMessage().getText());
        }
    }

    public void sendMessage(String usrId, String text) {
        SendMessage message = new SendMessage(usrId, text);

        try {
            execute(message);
            logger.info("Sent message to chat {}", usrId);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message", e);
        }
    }

    public void sendToAdmin(String text) {
        sendMessage(usrId, text);
        logger.info("Sent message to admin");
    }
}
