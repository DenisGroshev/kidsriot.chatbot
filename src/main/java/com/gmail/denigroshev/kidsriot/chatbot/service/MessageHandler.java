package com.gmail.denigroshev.kidsriot.chatbot.service;

import com.gmail.denigroshev.kidsriot.chatbot.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageHandler {

    private final TelegramMessageService messageService;
    private final KeyboardFactory keyboardFactory;

    public List<BotApiMethod<? extends Serializable>> handleMessage(Update update) {
        List<BotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        messages.add(messageService.createTextMessage(chatId,
                "Выберите интересующий вопрос 👇", keyboardFactory.getMainMenu()));
        return messages;
    }
}
