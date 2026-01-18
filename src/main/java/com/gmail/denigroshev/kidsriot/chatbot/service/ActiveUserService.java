package com.gmail.denigroshev.kidsriot.chatbot.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ActiveUserService {
    private final Map<Long, Boolean> activeUsers = new ConcurrentHashMap<>();

    public Boolean isActive(Long chatId) {
        return activeUsers.containsKey(chatId);
    }

    public void activate(Long chatId) {
        activeUsers.put(chatId, true);
    }
    public void deactivate(Long chatId) {
        activeUsers.remove(chatId);
    }
}
