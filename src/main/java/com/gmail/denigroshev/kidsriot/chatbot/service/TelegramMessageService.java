package com.gmail.denigroshev.kidsriot.chatbot.service;

import com.gmail.denigroshev.kidsriot.chatbot.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@RequiredArgsConstructor
public class TelegramMessageService {

    private final KeyboardFactory keyboardFactory;

    public SendMessage createTextMessage(Long chatId, String text, boolean withEndChatButton) {
        InlineKeyboardMarkup kb = withEndChatButton
                ? keyboardFactory.getChatControlMenu()
                : null;
        return createTextMessage(chatId, text, kb);
    }

    public SendMessage createTextMessage(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("HTML")
                .replyMarkup(keyboard)
                .build();
    }

    public EditMessageText editMessage(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
        return EditMessageText.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .text(text)
                .parseMode("HTML")
                .replyMarkup(keyboard)
                .build();
    }

    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId.toString())
                .messageId(messageId)
                .build();
    }
}