package com.gmail.denigroshev.kidsriot.chatbot.service;

import com.gmail.denigroshev.kidsriot.chatbot.factory.KeyboardFactory;
import com.gmail.denigroshev.kidsriot.chatbot.utilities.ChatIdExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackHandler {

    private final TelegramMessageService messageService;
    private final ActiveUserService activeUserService;
    private final KeyboardFactory keyboardFactory;
    private final OperatorService operatorService;

    @Value("${telegram.operator.chatId}")
    private Long operatorChatId;

    public List<BotApiMethod<? extends Serializable>> handleCallback(Update update) {
        List<BotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        CallbackQuery callback = update.getCallbackQuery();
        String data = callback.getData();
        Long chatId = callback.getMessage().getChatId();
        Integer messageId = callback.getMessage().getMessageId();

        switch (data) {
            // Меню навигация
            case "BACK_TO_MAIN" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "Выберите интересующий вопрос 👇",
                            keyboardFactory.getMainMenu())
            );

            case "MENU_PAYMENT" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "💳 Вопросы по оплате:",
                            keyboardFactory.getPaymentMenu())
            );

            case "MENU_DELIVERY" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "🚚 Вопросы по доставке:",
                            keyboardFactory.getDeliveryMenu())
            );

            case "MENU_RETURN" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "↩️ Вопросы по возврату:",
                            keyboardFactory.getReturnMenu())
            );

            // Оплата
            case "PAYMENT" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "💳 Не проходит оплата/не открывается окно оплаты:\n\n" +
                                    "Выключить VPN и заново оформить заказ.",
                            keyboardFactory.getBackButton("MENU_PAYMENT"))
            );

            case "PAYMENT2" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "💳 Оплата долями:\n\n" +
                                    "Если нет 18 лет, то банк не одобрит покупку Долями.\n\n" +
                                    "Если есть 18 лет, то убедитесь, что VPN выключен и оформите заказ без него.\n\n" +
                                    "Если есть 18 лет, VPN выключен, но оплата всё равно не проходит, " +
                                    "тогда нужно связаться с менеджером - возможно проблема в работе сервиса.",
                            keyboardFactory.getBackButton("MENU_PAYMENT"))
            );

            // Доставка
            case "DELIVERY" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "🚚 Как отслеживать заказ:\n\n" +
                                    "После оформления заказа мы отправим трек-номер, " +
                                    "по которому можно отслеживать заказ на сайте или в приложении СДЭК/Почты России.",
                            keyboardFactory.getBackButton("MENU_DELIVERY"))
            );

            case "DELIVERY_FRONTIER" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "🌍 Доставка в другие страны:\n\n" +
                                    "Доставляем через СДЭК в страны СНГ (Казахстан, Беларусь, Кыргызстан, Армения, Узбекистан, Азербайджан, Молдова).\n\n" +
                                    "Если нужна доставка в другую страну, нужно связаться с менеджером для уточнения такой возможности.",
                            keyboardFactory.getBackButton("MENU_DELIVERY"))
            );

            case "WHEN_DELIVERY" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "⏰ Когда отправят заказ:\n\n" +
                                    "При оформлении до 20:00 в будни и до 19:00 в выходные дни, отправка происходит в день заказа. " +
                                    "При заказе позднее этого времени, отправляем на следующий день.\n\n" +
                                    "Отправляем ежедневно, без выходных. В праздники отправка зависит от работы СДЭК и Почты России, уточнить можно у менеджера.",
                            keyboardFactory.getBackButton("MENU_DELIVERY"))
            );

            case "COURIER" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "🚗 Доставка курьером:\n\n" +
                                    "При выборе доставки курьером, " +
                                    "с вами свяжется менеджер для выбора удобного дня и времени.",
                            keyboardFactory.getBackButton("MENU_DELIVERY"))
            );

            // Возврат
            case "CLOTHE_BACK" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "↩️ Можно ли вернуть товар:\n\n" +
                                    "Да, в течение 14 дней после получения заказа.",
                            keyboardFactory.getBackButton("MENU_RETURN"))
            );

            case "HOW_TO_MONEY_BACK" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "📋 Как оформить возврат:\n\n" +
                                    "Для возврата нужно связаться с менеджером. " +
                                    "Товар должен быть в первоначальном виде. Обратную доставку оплачивает покупатель.",
                            keyboardFactory.getBackButton("MENU_RETURN"))
            );

            case "WHEN_MONEY_BACK" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "💰 Когда вернутся деньги:\n\n" +
                                    "После получения товара, одобряется возврат, и деньги возвращаются в среднем за 3 рабочих дня, в зависимости от банка. " +
                                    "Возврат включает в себя стоимость товара без учета стоимости доставки.",
                            keyboardFactory.getBackButton("MENU_RETURN"))
            );

            // Простые ответы
            case "SALES" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "🎁 Скидки и акции:\n\n" +
                                    "Актуальные скидки публикуем в нашем тг-канале @kids_riot",
                            keyboardFactory.getBackButton("BACK_TO_MAIN"))
            );

            case "COLAB" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "🤝 Сотрудничество:\n\n" +
                                    "Предложения о сотрудничестве отправляйте на email или через оператора.",
                            keyboardFactory.getBackButton("BACK_TO_MAIN"))
            );

            // Работа с оператором
            case "OPERATOR" -> messages.addAll(handleOperatorRequest(chatId, messageId, update));
            case "END_CHAT" -> messages.addAll(handleEndChat(chatId, callback.getMessage().toString(), update));

            default -> messages.add(
                    messageService.createTextMessage(chatId, "❓ Неизвестная команда.", false)
            );
        }
        return messages;
    }

    private List<BotApiMethod<? extends Serializable>> handleOperatorRequest(Long chatId, Integer messageId, Update update) {
        List<BotApiMethod<? extends Serializable>> messages = new ArrayList<>();

        if (activeUserService.isActive(chatId)) {
            messages.add(messageService.editMessage(chatId, messageId,
                    "⏳ Диалог уже активен.",
                    keyboardFactory.getChatControlMenu()));
            return messages;
        }

        activeUserService.activate(chatId);

        User user = update.getCallbackQuery().getFrom();
        String username = user.getUserName() != null ? user.getUserName() : user.getFirstName();

        String notification = operatorService.notifyOperator(chatId, username);

        messages.add(messageService.createTextMessage(operatorChatId, notification, true));

        messages.add(messageService.editMessage(chatId, messageId,
                "✅ Оператор уведомлён!\n\n" +
                        "Можете писать ваши вопросы, оператор ответит в ближайшее время.",
                keyboardFactory.getChatControlMenu()));

        return messages;
    }

    private List<BotApiMethod<? extends Serializable>> handleEndChat(Long chatId, String messageText, Update update) {
        List<BotApiMethod<? extends Serializable>> messages = new ArrayList<>();

        if (chatId.equals(operatorChatId)) {
            Long userId = ChatIdExtractor.extract(messageText);

            if (userId == null && update.hasCallbackQuery()) {
                String callbackText = update.getCallbackQuery().getMessage().toString();
                userId = ChatIdExtractor.extract(callbackText);
            }

            if (userId != null && activeUserService.isActive(userId)) {
                activeUserService.deactivate(userId);

                messages.add(messageService.createTextMessage(
                        operatorChatId,
                        "✅ Диалог с пользователем " + userId + " завершён.",
                        false
                ));

                messages.add(messageService.createTextMessage(
                        userId,
                        "⚠️ Оператор завершил диалог.\n\nВыберите интересующий вопрос 👇",
                        keyboardFactory.getMainMenu()
                ));
            } else {
                messages.add(messageService.createTextMessage(
                        operatorChatId,
                        "⚠️ Не удалось завершить диалог. Ответьте на сообщение пользователя с Chat ID.",
                        false
                ));
            }
        } else {
            if (activeUserService.isActive(chatId)) {
                activeUserService.deactivate(chatId);

                messages.add(messageService.createTextMessage(
                        operatorChatId,
                        "⚠️ Пользователь " + chatId + " завершил диалог.",
                        true
                ));

                messages.add(messageService.createTextMessage(
                        chatId,
                        "✅ Диалог завершён.\n\nВыберите интересующий вопрос 👇",
                        keyboardFactory.getMainMenu()
                ));
            } else {
                messages.add(messageService.createTextMessage(
                        chatId,
                        "ℹ️ У вас нет активного диалога с оператором.",
                        false
                ));
            }
        }
        return messages;
    }
}