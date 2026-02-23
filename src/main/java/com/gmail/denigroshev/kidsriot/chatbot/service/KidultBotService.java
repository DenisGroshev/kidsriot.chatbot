package com.gmail.denigroshev.kidsriot.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@Service
public class KidultBotService extends TelegramLongPollingBot {

    private final String botUsername;
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

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
            if (update.hasMessage() && update.getMessage().hasText()) {
                executeMethods(messageHandler.handleMessage(update));
            } else if (update.hasCallbackQuery()) {
                if ("PROMO_FEB23".equals(update.getCallbackQuery().getData())) {
                    callbackHandler.handlePromoAnimation(update, this);
                } else {
                    executeMethods(callbackHandler.handleCallback(update));
                }
            }
        } catch (Exception e) {
            log.error("Error processing update: {}", update, e);
        }
    }

    public void executeMethods(List<BotApiMethod<? extends Serializable>> methods) {
        for (BotApiMethod<? extends Serializable> method : methods) {
            try {
                if (method instanceof SendMessage sm) {
                    execute(sm);
                } else if (method instanceof EditMessageText emt) {
                    execute(emt);
                } else if (method instanceof DeleteMessage dm) {
                    execute(dm);
                }
            } catch (Exception e) {
                log.error("Error executing method: {}", e.getMessage());
            }
        }
    }

    public void scheduleEdit(EditMessageText edit, long delayMs) {
        scheduler.schedule(() -> {
            try {
                execute(edit);
            } catch (Exception e) {
                log.error("Error during scheduled edit: {}", e.getMessage());
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    public void scheduleEditCaption(EditMessageCaption edit, long delayMs) {
        scheduler.schedule(() -> {
            try {
                execute(edit);
            } catch (Exception e) {
                log.error("Error during scheduled caption edit: {}", e.getMessage());
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    public void deleteAndSendPhoto(Long chatId, Integer messageId, SendPhoto photo,
                                   Consumer<Integer> onPhotoSent) {
        try {
            execute(DeleteMessage.builder()
                    .chatId(chatId.toString())
                    .messageId(messageId)
                    .build());
            var sentMessage = execute(photo);
            onPhotoSent.accept(sentMessage.getMessageId());
        } catch (Exception e) {
            log.error("Error during delete+photo: {}", e.getMessage());
        }
    }
}