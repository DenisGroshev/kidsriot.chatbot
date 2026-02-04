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
                button("О проекте. Манифест", "MENU_ABOUT")
        ));

        keyboard.add(List.of(
                button("Оплата", "MENU_PAYMENT"),
                button("Доставка", "MENU_DELIVERY")
        ));

        keyboard.add(List.of(
                button("Возврат", "MENU_RETURN"),
                button("Размеры", "MENU_SIZES")
        ));

        keyboard.add(List.of(
                button("Отзывы", "MENU_REVIEWS"),
                button("Связь", "MENU_CONTACT")
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
                button("Доставка в другие страны", "DELIVERY_FRONTIER")
        ));

        keyboard.add(List.of(
                button("Сроки", "WHEN_DELIVERY")
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
                button("Как оформить возврат?", "CLOTHE_BACK")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getContactMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Поддержка", "CONTACT_SUPPORT"),
                button("Предложения", "CONTACT_PROPOSALS")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getSupportContactMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                urlButton("Связаться с поддержкой", "https://t.me/kidsriot_support")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "MENU_CONTACT")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getProposalsContactMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                urlButton("Написать менеджеру", "https://t.me/janni_rodari")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "MENU_CONTACT")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getSizesMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("TRACK JEANS", "SIZES_TRACK_JEANS")
        ));

        keyboard.add(List.of(
                button("RIPPED ZIP", "SIZES_RIPPED_ZIP")
        ));

        keyboard.add(List.of(
                button("RIPPED PANTS", "SIZES_RIPPED_PANTS")
        ));

        keyboard.add(List.of(
                button("POLO 5", "SIZES_POLO_5")
        ));

        keyboard.add(List.of(
                button("BEANIE 5X5", "SIZES_BEANIE_5X5")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getTrackJeansSizesMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Размер S", "SIZE_TRACK_JEANS_S"),
                button("Размер M", "SIZE_TRACK_JEANS_M")
        ));

        keyboard.add(List.of(
                button("Размер L", "SIZE_TRACK_JEANS_L"),
                button("Размер XL", "SIZE_TRACK_JEANS_XL")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "MENU_SIZES")
        ));

        keyboard.add(List.of(
                button("🏠 Главное меню", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getRippedZipSizesMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Размер S", "SIZE_RIPPED_ZIP_S"),
                button("Размер M", "SIZE_RIPPED_ZIP_M")
        ));

        keyboard.add(List.of(
                button("Размер L", "SIZE_RIPPED_ZIP_L"),
                button("Размер XL", "SIZE_RIPPED_ZIP_XL")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "MENU_SIZES")
        ));

        keyboard.add(List.of(
                button("🏠 Главное меню", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getRippedPantsSizesMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Размер S", "SIZE_RIPPED_PANTS_S"),
                button("Размер M", "SIZE_RIPPED_PANTS_M")
        ));

        keyboard.add(List.of(
                button("Размер L", "SIZE_RIPPED_PANTS_L"),
                button("Размер XL", "SIZE_RIPPED_PANTS_XL")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "MENU_SIZES")
        ));

        keyboard.add(List.of(
                button("🏠 Главное меню", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getPolo5SizesMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Размер S", "SIZE_POLO_5_S"),
                button("Размер M", "SIZE_POLO_5_M")
        ));

        keyboard.add(List.of(
                button("Размер L", "SIZE_POLO_5_L"),
                button("Размер XL", "SIZE_POLO_5_XL")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "MENU_SIZES")
        ));

        keyboard.add(List.of(
                button("🏠 Главное меню", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getBeanieSizesMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("Единый размер", "SIZE_BEANIE_5X5_ONE")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", "MENU_SIZES")
        ));

        keyboard.add(List.of(
                button("🏠 Главное меню", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getSizeDetailsMenu(String backCallback) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                button("⬅️ Назад", backCallback)
        ));

        keyboard.add(List.of(
                button("🏠 Главное меню", "BACK_TO_MAIN")
        ));

        return new InlineKeyboardMarkup(keyboard);
    }

    public InlineKeyboardMarkup getBackButton(String backCallback) {
        return new InlineKeyboardMarkup(
                List.of(List.of(button("⬅️ Назад", backCallback)))
        );
    }

    public InlineKeyboardMarkup getBackWithOperatorButton(String backCallback) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(List.of(
                urlButton("Связаться с поддержкой", "https://t.me/kidsriot_support")
        ));

        keyboard.add(List.of(
                button("⬅️ Назад", backCallback)
        ));

        return new InlineKeyboardMarkup(keyboard);
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

    private InlineKeyboardButton urlButton(String text, String url) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setUrl(url);
        return button;
    }
}
