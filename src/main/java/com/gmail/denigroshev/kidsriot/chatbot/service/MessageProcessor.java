package com.gmail.denigroshev.kidsriot.chatbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageProcessor {
    void process(Update update);
}
