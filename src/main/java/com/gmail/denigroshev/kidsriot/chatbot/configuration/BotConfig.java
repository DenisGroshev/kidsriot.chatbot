package com.gmail.denigroshev.kidsriot.chatbot.configuration;

import com.gmail.denigroshev.kidsriot.chatbot.service.KidultBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    @Bean
    public ApplicationRunner registerBot(TelegramBotsApi telegramBotsApi, KidultBotService kidultBotService) {
        return args -> {
            telegramBotsApi.registerBot(kidultBotService);
            log.info("✅ Telegram bot '{}' successfully registered!", kidultBotService.getBotUsername());
        };
    }
}