package com.gmail.denigroshev.kidsriot.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KidultBotService extends TelegramLongPollingBot {

    private final String botUsername;
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;

    public KidultBotService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            MessageHandler messageHandler,
            CallbackHandler callbackHandler
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.messageHandler = messageHandler;
        this.callbackHandler = callbackHandler;
        log.info("KidultBotService initialized with username: {}", botUsername);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            List<BotApiMethod<? extends Serializable>> methods = new ArrayList<>();

            if (update.hasMessage() && update.getMessage().hasText()) {
                methods = messageHandler.handleMessage(update);
            } else if (update.hasCallbackQuery()) {
                methods = callbackHandler.handleCallback(update);
            }

            for (BotApiMethod<? extends Serializable> method : methods) {
                if (method instanceof SendMessage) {
                    execute((SendMessage) method);
                } else if (method instanceof EditMessageText) {
                    execute((EditMessageText) method);
                }
            }
        } catch (Exception e) {
            log.error("Error processing update: {}", update, e);
        }
    }
}