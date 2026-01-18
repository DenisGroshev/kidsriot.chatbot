package com.gmail.denigroshev.kidsriot.chatbot.service;

import org.springframework.stereotype.Service;

@Service
public class OperatorService {

    public String notifyOperator(Long userChatId, String username) {
        return """
                📞 Новый запрос оператора
                
                👤 Пользователь: %s
                🆔 Chat ID: %d
                """.formatted(username != null ? username : "без юзернейма", userChatId);
    }
}