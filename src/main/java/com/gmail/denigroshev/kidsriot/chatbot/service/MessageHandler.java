package com.gmail.denigroshev.kidsriot.chatbot.service;

import com.gmail.denigroshev.kidsriot.chatbot.factory.KeyboardFactory;
import com.gmail.denigroshev.kidsriot.chatbot.utilities.ChatIdExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final ActiveUserService activeUserService;
    private final KeyboardFactory keyboardFactory;

    @Value("${telegram.operator.chatId}")
    private Long operatorChatId;

    public List<BotApiMethod<? extends Serializable>> handleMessage(Update update) {
        List<BotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();

        if (chatId.equals(operatorChatId)) {
            messages.addAll(handleOperatorMessage(message));
        } else {
            messages.addAll(handleUserMessage(chatId, text));
        }
        return messages;
    }

    private List<BotApiMethod<? extends Serializable>> handleOperatorMessage(Message message) {
        List<BotApiMethod<? extends Serializable>> messages = new ArrayList<>();

        if (!message.isReply()) {
            messages.add(
                    messageService.createTextMessage(operatorChatId,
                            "⚠️ Ответьте на сообщение пользователя через Reply.", false)
            );
            return messages;
        }

        String repliedText = message.getReplyToMessage().getText();
        Long userChatId = ChatIdExtractor.extract(repliedText);

        if (userChatId != null && activeUserService.isActive(userChatId)) {
            messages.add(
                    messageService.createTextMessage(userChatId,
                            "💬 Взрослый ребенок: " + message.getText(), true)
            );
        } else {
            messages.add(
                    messageService.createTextMessage(operatorChatId,
                            "⚠️ Не удалось определить активного пользователя для ответа.", false)
            );
        }
        return messages;
    }

    private List<BotApiMethod<? extends Serializable>> handleUserMessage(Long chatId, String text) {
        List<BotApiMethod<? extends Serializable>> messages = new ArrayList<>();

        if (activeUserService.isActive(chatId)) {
            String forwarded = "💬 Пользователь: " + text + "\n🆔 Chat ID: " + chatId;
            messages.add(messageService.createTextMessage(operatorChatId, forwarded, true));
        } else {
            messages.add(messageService.createTextMessage(chatId,
                    "Выберите интересующий вопрос 👇", keyboardFactory.getMainMenu()));
        }
        return messages;
    }
}