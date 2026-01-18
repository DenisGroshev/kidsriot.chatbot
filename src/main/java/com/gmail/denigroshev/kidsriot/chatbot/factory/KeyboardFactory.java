package com.gmail.denigroshev.kidsriot.chatbot.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardFactory {

    public InlineKeyboardMarkup getMainMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("💳 Оплата", "MENU_PAYMENT"),
                button("🚚 Доставка", "MENU_DELIVERY")
        ));

        keyboard.add(List.of(
                button("↩️ Возврат", "MENU_RETURN"),
                button("🎁 Скидки", "SALES")
        ));

        keyboard.add(List.of(
                button("🤝 Сотрудничество", "COLAB"),
                button("📞 Оператор", "OPERATOR")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getPaymentMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Проблема с оплатой", "PAYMENT")
        ));

        keyboard.add(List.of(
                button("Проблема с оплатой долями", "PAYMENT2")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getDeliveryMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Отслеживание заказа", "DELIVERY")
        ));

        keyboard.add(List.of(
                button("Другие страны", "DELIVERY_FRONTIER")
        ));

        keyboard.add(List.of(
                button("Когда отправят?", "WHEN_DELIVERY")
        ));

        keyboard.add(List.of(
                button("Доставка курьером", "COURIER")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getReturnMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Можно ли вернуть?", "CLOTHE_BACK")
        ));

        keyboard.add(List.of(
                button("Как оформить?", "HOW_TO_MONEY_BACK")
        ));

        keyboard.add(List.of(
                button("Когда вернут деньги?", "WHEN_MONEY_BACK")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getBackButton(String backCallback) {
        return new InlineKeyboardMarkup(
                List.of(List.of(button("⬅️ Назад", backCallback)))
        );
    }

    public InlineKeyboardMarkup getChatControlMenu() {
        return new InlineKeyboardMarkup(
                List.of(List.of(button("❌ Завершить диалог", "END_CHAT")))
        );
    }

    private InlineKeyboardButton button(String text, String callback) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callback);
        return button;
    }
}