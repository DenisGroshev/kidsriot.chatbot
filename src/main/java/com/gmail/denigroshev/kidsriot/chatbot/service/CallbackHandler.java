package com.gmail.denigroshev.kidsriot.chatbot.service;

import com.gmail.denigroshev.kidsriot.chatbot.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackHandler {

    private final TelegramMessageService messageService;
    private final KeyboardFactory keyboardFactory;
    private final PromoCodeService promoCodeService;

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 12;
    private static final Random RANDOM = new Random();

    private String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public void handlePromoAnimation(Update update, KidultBotService bot) {
        CallbackQuery callback = update.getCallbackQuery();
        Long chatId = callback.getMessage().getChatId();
        Integer messageId = callback.getMessage().getMessageId();

        PromoCodeService.PromoResult result = promoCodeService.requestPromoCode(chatId);

        if (result.getStatus() != PromoCodeService.PromoResult.Status.ISSUED) {

            if (result.getStatus() == PromoCodeService.PromoResult.Status.ALREADY_ISSUED) {
                try {
                    InputStream photoStream = new ClassPathResource("promo.PNG").getInputStream();

                    String caption = "🎰 <b>ТВОЙ ВЫИГРЫШ:</b>\n\n" +
                            "<b>🔥Скидка:</b> " + result.getDiscount() + "\n" +
                            "<b>🍒Промокод:</b> <code>" + result.getCode() + "</code>\n\n" +
                            "Додепа не будет 😭 Забирай свой бонус и УСПЕЙ активировать его до 27.02 💰💰\n" +
                            "‼️Введи его в корзине при оформлении заказа на сайте kidsriot.ru и суммируй со скидкой -23% 🤑";

                    SendPhoto sendPhoto = SendPhoto.builder()
                            .chatId(chatId.toString())
                            .photo(new InputFile(photoStream, "promo.PNG"))
                            .caption(caption)
                            .parseMode("HTML")
                            .replyMarkup(keyboardFactory.getBackButton("BACK_TO_MAIN"))
                            .build();

                    bot.deleteAndSendPhoto(chatId, messageId, sendPhoto, photoMessageId -> {});

                } catch (Exception e) {
                    log.error("Failed to load promo photo for already issued: {}", e.getMessage());

                    // Fallback — текст без фото
                    bot.executeMethods(List.of(
                            messageService.editMessage(chatId, messageId,
                                    "🎰 <b>ТВОЙ ВЫИГРЫШ:</b>\n\n" +
                                            "<b>🔥Скидка:</b> " + result.getDiscount() + "\n" +
                                            "<b>🍒Промокод:</b> <code>" + result.getCode() + "</code>\n\n" +
                                            "Додепа не будет 😭 Забирай свой бонус и УСПЕЙ активировать его до 27.02 💰💰\n" +
                                            "‼️Введи его в корзине при оформлении заказа на сайте kidsriot.ru и суммируй со скидкой -23% 🤑",
                                    keyboardFactory.getBackButton("BACK_TO_MAIN"))
                    ));
                }
                return;
            }

            // EXHAUSTED — без фото
            if (result.getStatus() == PromoCodeService.PromoResult.Status.EXHAUSTED) {
                bot.executeMethods(List.of(
                        messageService.editMessage(chatId, messageId,
                                "🥲<b>Упс… К сожалению, спины закончились - все промокоды уже разобрали.</b>\n\n" +
                                        "Следи за акциями и новыми дропами в нашем тгк @kids_riot - впереди много интересного ^^",
                                keyboardFactory.getBackButton("BACK_TO_MAIN"))
                ));
                return;
            }
        }

        // === ШАГ 1: удаляем текст, сразу отправляем фото с начальной подписью ===
        try {
            InputStream photoStream = new ClassPathResource("promo.PNG").getInputStream();

            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId.toString())
                    .photo(new InputFile(photoStream, "promo.PNG"))
                    .caption("<b>Делаем прокрутик...\uD83D\uDE4F\uD83D\uDE4F</b>\n\n" +
                            "<code>" + randomString(CODE_LENGTH) + "</code>\n\n" +
                            "<i>Опа, кажись занесет \uD83E\uDD11\uD83E\uDD11\uD83E\uDD11</i>")
                    .parseMode("HTML")
                    .build();

            bot.deleteAndSendPhoto(chatId, messageId, sendPhoto, photoMessageId -> {

                // === ШАГ 2: анимация в подписи фото ===
                int animationSteps = 5;
                long stepDelay = 350;

                for (int i = 1; i <= animationSteps; i++) {
                    EditMessageCaption frame = EditMessageCaption.builder()
                            .chatId(chatId.toString())
                            .messageId(photoMessageId)
                            .caption("<b>Делаем прокрутик...\uD83D\uDE4F\uD83D\uDE4F</b>\n\n" +
                                    "<code>" + randomString(CODE_LENGTH) + "</code>\n\n" +
                                    "<i>Опа, кажись занесет \uD83E\uDD11\uD83E\uDD11\uD83E\uDD11</i>")
                            .parseMode("HTML")
                            .build();
                    bot.scheduleEditCaption(frame, (long) i * stepDelay);
                }

                // === ШАГ 3: финальный промокод в подписи ===
                long finalDelay = (long) animationSteps * stepDelay + 200;

                EditMessageCaption finalCaption = EditMessageCaption.builder()
                        .chatId(chatId.toString())
                        .messageId(photoMessageId)
                        .caption("<b>\uD83C\uDFB0 ТВОЙ ВЫИГРЫШ:</b>\n\n" +
                                "<b>\uD83D\uDD25Скидка:</b> <b>" + result.getDiscount() + "</b>\n" +
                                "<b>\uD83C\uDF52Промокод:</b> <code>" + result.getCode() + "</code>\n\n" +
                                "Поздравляем с 23 февраля! Это уникальный РАЗОВЫЙ промик только для тебя, действующий до 27.02 \uD83D\uDE08 \n\n" +
                                "‼\uFE0F Введи его в корзине при оформлении заказа на сайте kidsriot.ru и суммируй со скидкой -23% \uD83E\uDD11")
                        .parseMode("HTML")
                        .replyMarkup(keyboardFactory.getBackButton("BACK_TO_MAIN"))
                        .build();

                bot.scheduleEditCaption(finalCaption, finalDelay);
            });

        } catch (Exception e) {
            log.error("Failed to load promo photo: {}", e.getMessage());

            // Fallback — текст без фото
            int animationSteps = 6;
            long stepDelay = 350;
            for (int i = 0; i < animationSteps; i++) {
                EditMessageText frame = messageService.editMessage(chatId, messageId,
                        "<b>Делаем прокрутик...\uD83D\uDE4F\uD83D\uDE4F</b>\n\n" +
                                "<code>" + randomString(CODE_LENGTH) + "</code>\n\n" +
                                "<i>Опа, кажись занесет \uD83E\uDD11\uD83E\uDD11\uD83E\uDD11</i>", null);
                bot.scheduleEdit(frame, (long) i * stepDelay);
            }
            EditMessageText fallback = messageService.editMessage(chatId, messageId,
                    "<b>\uD83C\uDFB0 ТВОЙ ВЫИГРЫШ:</b>\n\n" +
                            "<b>\uD83D\uDD25Скидка:</b> <b>" + result.getDiscount() + "</b>\n" +
                            "<b>\uD83C\uDF52Промокод:</b> <code>" + result.getCode() + "</code>\n\n" +
                            "Поздравляем с 23 февраля! Это уникальный РАЗОВЫЙ промик только для тебя, действующий до 27.02 \uD83D\uDE08 \n\n" +
                            "‼\uFE0F Введи его в корзине при оформлении заказа на сайте kidsriot.ru и суммируй со скидкой -23% \uD83E\uDD11",
                    keyboardFactory.getBackButton("BACK_TO_MAIN"));
            bot.scheduleEdit(fallback, (long) animationSteps * stepDelay + 200);
        }
    }

    public List<BotApiMethod<? extends Serializable>> handleCallback(Update update) {
        List<BotApiMethod<? extends Serializable>> messages = new ArrayList<>();
        CallbackQuery callback = update.getCallbackQuery();
        String data = callback.getData();
        Long chatId = callback.getMessage().getChatId();
        Integer messageId = callback.getMessage().getMessageId();

        switch (data) {
            case "BACK_TO_MAIN" -> {
                boolean isPhoto = false;
                if (callback.getMessage() instanceof org.telegram.telegrambots.meta.api.objects.Message msg) {
                    isPhoto = msg.hasPhoto();
                }

                if (isPhoto) {
                    messages.add(messageService.deleteMessage(chatId, messageId));
                    messages.add(messageService.createTextMessage(chatId,
                            "Выберите интересующий вопрос 👇",
                            keyboardFactory.getMainMenu()));
                } else {
                    messages.add(messageService.editMessage(chatId, messageId,
                            "Выберите интересующий вопрос 👇",
                            keyboardFactory.getMainMenu()));
                }
            }

            case "MENU_ABOUT" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>О проекте. Манифест</b>\n\n" +
                                    "Бунт детей - это команда молодых креаторов, создающая яркий стритвир без правил. \n" +
                                    "\n" +
                                    "Мы увлечены идеей создавать строго свой, на 100% уникальный продукт, поэтому мы не копируем чужие идеи. Каждая вещь в нашем ассортименте - это вызов скучной «взрослой» жизни и вторичным трендам. \n" +
                                    "\n" +
                                    "Наш бренд создан для таких же, как и мы: молодых, безбашенных и креативных ребят, создающих новое в своей сфере и меняющих правила игры прямо сейчас.  \n" +
                                    "\n" +
                                    "Присоединяйся к коммьюнити - вместе мы победим!\n" +
                                    "\n" +
                                    "<i>LONG LIVE KIDS RIOT</i>",
                            keyboardFactory.getBackButton("BACK_TO_MAIN"))
            );

            case "MENU_PAYMENT" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>💳 Вопросы по оплате:</b>",
                            keyboardFactory.getPaymentMenu())
            );

            case "MENU_DELIVERY" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>🚚 Вопросы по доставке:</b>",
                            keyboardFactory.getDeliveryMenu())
            );

            case "MENU_RETURN" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Возврат</b>\n\n" +
                                    "Заявления на возврат принимаются в поддержке: @kidsriot_support.\n\n" +
                                    "Важно! Пожалуйста, убедись:\n\n" +
                                    "- Вещь сохранила свой первоначальный вид\n" +
                                    "- С момента получения товара прошло менее 14 дней\n" +
                                    "- Это не шапка или другие аксессуары, которые мы не сможем принять обратно по закону\n\n" +
                                    "Обратную доставку оплачивает покупатель. После получения товара, мы проверяем его " +
                                    "и если всё ок, сразу же оформляем возврат. А дальше уже зависит от банка: " +
                                    "обычно 3 рабочих дня и сумма (без учета доставки) возвращается на счёт.",
                            keyboardFactory.getBackWithOperatorButton("BACK_TO_MAIN"))
            );

            case "MENU_SIZES" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Размеры</b>\n\n" +
                                    "Чтобы посмотреть замеры всех товаров и рекомендации по росту, нажми на интересующую вещь. " +
                                    "Если останутся вопросы - будем рады помочь в поддержке: @kidsriot_support. " +
                                    "Поможем, подскажем, подберем идеальный вариант!\n\n" +
                                    "Выбери товар:",
                            keyboardFactory.getSizesMenu())
            );

            case "SIZES_TRACK_JEANS" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>TRACK JEANS</b>\n\n" +
                                    "Выбери размер:",
                            keyboardFactory.getTrackJeansSizesMenu())
            );

            case "SIZES_RIPPED_ZIP" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED ZIP</b>\n\n" +
                                    "Выбери размер:",
                            keyboardFactory.getRippedZipSizesMenu())
            );

            case "SIZES_RIPPED_PANTS" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED PANTS</b>\n\n" +
                                    "Выбери размер:",
                            keyboardFactory.getRippedPantsSizesMenu())
            );

            case "SIZES_POLO_5" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>POLO 5</b>\n\n" +
                                    "Выбери размер:",
                            keyboardFactory.getPolo5SizesMenu())
            );

            case "SIZES_BEANIE_5X5" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>BEANIE 5X5</b>\n\n" +
                                    "Выбери размер:",
                            keyboardFactory.getBeanieSizesMenu())
            );

            case "SIZE_TRACK_JEANS_S" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>TRACK JEANS — Размер S</b>\n\n" +
                                    "Полуобхват пояса (см) – 38\n" +
                                    "Длина штанов (см) – 103\n" +
                                    "Ширина штанины (см) – 31\n\n" +
                                    "S - на рост до 165 см",
                            keyboardFactory.getSizeDetailsMenu("SIZES_TRACK_JEANS"))
            );

            case "SIZE_TRACK_JEANS_M" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>TRACK JEANS — Размер M</b>\n\n" +
                                    "Полуобхват пояса (см) – 40\n" +
                                    "Длина штанов (см) – 105\n" +
                                    "Ширина штанины (см) – 31\n\n" +
                                    "M - на рост от 165 до 175 см",
                            keyboardFactory.getSizeDetailsMenu("SIZES_TRACK_JEANS"))
            );

            case "SIZE_TRACK_JEANS_L" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>TRACK JEANS — Размер L</b>\n\n" +
                                    "Полуобхват пояса (см) – 42\n" +
                                    "Длина штанов (см) – 107\n" +
                                    "Ширина штанины (см) – 31\n\n" +
                                    "L - на рост от 175 см до 185 см",
                            keyboardFactory.getSizeDetailsMenu("SIZES_TRACK_JEANS"))
            );

            case "SIZE_TRACK_JEANS_XL" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>TRACK JEANS — Размер XL</b>\n\n" +
                                    "Полуобхват пояса (см) – 44\n" +
                                    "Длина штанов (см) – 109\n" +
                                    "Ширина штанины (см) – 32\n\n" +
                                    "XL - на рост от 185 см",
                            keyboardFactory.getSizeDetailsMenu("SIZES_TRACK_JEANS"))
            );

            case "SIZE_RIPPED_ZIP_S" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED ZIP — Размер S</b>\n\n" +
                                    "Длина (cм) – 60\n" +
                                    "Ширина (см) – 60\n" +
                                    "Длина рукава (см) – 57\n\n" +
                                    "S - на рост до 165 см\n\n" +
                                    "К зип-худи есть подходящие штаны. Собери полный комплект, пока вещи в наличии:\n" +
                                    "https://kidsriot.ru/tproduct/562311780092-ripped-pants-in-grey",
                            keyboardFactory.getSizeDetailsMenu("SIZES_RIPPED_ZIP"))
            );

            case "SIZE_RIPPED_ZIP_M" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED ZIP — Размер M</b>\n\n" +
                                    "Длина (cм) – 62\n" +
                                    "Ширина (см) – 62\n" +
                                    "Длина рукава (см) – 58\n\n" +
                                    "M - на рост от 165 до 175 см\n\n" +
                                    "К зип-худи есть подходящие штаны. Собери полный комплект, пока вещи в наличии:\n" +
                                    "https://kidsriot.ru/tproduct/562311780092-ripped-pants-in-grey",
                            keyboardFactory.getSizeDetailsMenu("SIZES_RIPPED_ZIP"))
            );

            case "SIZE_RIPPED_ZIP_L" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED ZIP — Размер L</b>\n\n" +
                                    "Длина (cм) – 63\n" +
                                    "Ширина (см) – 66\n" +
                                    "Длина рукава (см) – 58\n\n" +
                                    "L - на рост от 175 см до 185 см\n\n" +
                                    "К зип-худи есть подходящие штаны. Собери полный комплект, пока вещи в наличии:\n" +
                                    "https://kidsriot.ru/tproduct/562311780092-ripped-pants-in-grey",
                            keyboardFactory.getSizeDetailsMenu("SIZES_RIPPED_ZIP"))
            );

            case "SIZE_RIPPED_ZIP_XL" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED ZIP — Размер XL</b>\n\n" +
                                    "Длина (cм) – 66\n" +
                                    "Ширина (см) – 70\n" +
                                    "Длина рукава (см) – 62\n\n" +
                                    "XL - на рост от 185 см\n\n" +
                                    "К зип-худи есть подходящие штаны. Собери полный комплект, пока вещи в наличии:\n" +
                                    "https://kidsriot.ru/tproduct/562311780092-ripped-pants-in-grey",
                            keyboardFactory.getSizeDetailsMenu("SIZES_RIPPED_ZIP"))
            );

            case "SIZE_RIPPED_PANTS_S" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED PANTS — Размер S</b>\n\n" +
                                    "Полуобхват пояса (см) – 29\n" +
                                    "Длина штанов (см) – 100\n" +
                                    "Ширина штанины (см) – 22\n\n" +
                                    "S - на рост до 165 см\n\n" +
                                    "К этим штанам идеально подойдет зип-худи. Собери полный комплект, пока вещи в наличии:\n" +
                                    "https://kidsriot.ru/tproduct/663436405322-ripped-zip-in-grey",
                            keyboardFactory.getSizeDetailsMenu("SIZES_RIPPED_PANTS"))
            );

            case "SIZE_RIPPED_PANTS_M" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED PANTS — Размер M</b>\n\n" +
                                    "Полуобхват пояса (см) – 30\n" +
                                    "Длина штанов (см) – 102\n" +
                                    "Ширина штанины (см) – 24\n\n" +
                                    "M - на рост от 165 до 175 см\n\n" +
                                    "К этим штанам идеально подойдет зип-худи. Собери полный комплект, пока вещи в наличии:\n" +
                                    "https://kidsriot.ru/tproduct/663436405322-ripped-zip-in-grey",
                            keyboardFactory.getSizeDetailsMenu("SIZES_RIPPED_PANTS"))
            );

            case "SIZE_RIPPED_PANTS_L" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED PANTS — Размер L</b>\n\n" +
                                    "Полуобхват пояса (см) – 32\n" +
                                    "Длина штанов (см) – 103\n" +
                                    "Ширина штанины (см) – 24\n\n" +
                                    "L - на рост от 175 см до 185 см\n\n" +
                                    "К этим штанам идеально подойдет зип-худи. Собери полный комплект, пока вещи в наличии:\n" +
                                    "https://kidsriot.ru/tproduct/663436405322-ripped-zip-in-grey",
                            keyboardFactory.getSizeDetailsMenu("SIZES_RIPPED_PANTS"))
            );

            case "SIZE_RIPPED_PANTS_XL" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>RIPPED PANTS — Размер XL</b>\n\n" +
                                    "Полуобхват пояса (см) – 34\n" +
                                    "Длина штанов (см) – 105\n" +
                                    "Ширина штанины (см) – 24\n\n" +
                                    "XL - на рост от 185 см\n\n" +
                                    "К этим штанам идеально подойдет зип-худи. Собери полный комплект, пока вещи в наличии:\n" +
                                    "https://kidsriot.ru/tproduct/663436405322-ripped-zip-in-grey",
                            keyboardFactory.getSizeDetailsMenu("SIZES_RIPPED_PANTS"))
            );

            case "SIZE_POLO_5_S" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>POLO 5 — Размер S</b>\n\n" +
                                    "Длина (см) – 61\n" +
                                    "Ширина (см) – 48\n" +
                                    "Длина рукава (см) – 51\n\n" +
                                    "S - на рост до 165 см",
                            keyboardFactory.getSizeDetailsMenu("SIZES_POLO_5"))
            );

            case "SIZE_POLO_5_M" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>POLO 5 — Размер M</b>\n\n" +
                                    "Длина (см) – 63\n" +
                                    "Ширина (см) – 50\n" +
                                    "Длина рукава (см) – 55\n\n" +
                                    "M - на рост от 165 до 175 см",
                            keyboardFactory.getSizeDetailsMenu("SIZES_POLO_5"))
            );

            case "SIZE_POLO_5_L" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>POLO 5 — Размер L</b>\n\n" +
                                    "Длина (см) – 66\n" +
                                    "Ширина (см) – 54\n" +
                                    "Длина рукава (см) – 55\n\n" +
                                    "L - на рост от 175 см до 185 см",
                            keyboardFactory.getSizeDetailsMenu("SIZES_POLO_5"))
            );

            case "SIZE_POLO_5_XL" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>POLO 5 — Размер XL</b>\n\n" +
                                    "Длина (см) – 69\n" +
                                    "Ширина (см) – 55\n" +
                                    "Длина рукава (см) – 56\n\n" +
                                    "XL - на рост от 185 см",
                            keyboardFactory.getSizeDetailsMenu("SIZES_POLO_5"))
            );

            case "SIZE_BEANIE_5X5_ONE" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>BEANIE 5X5 — Единый размер</b>\n\n" +
                                    "Длина (см) – 20\n" +
                                    "Ширина (см) – 20",
                            keyboardFactory.getSizeDetailsMenu("SIZES_BEANIE_5X5"))
            );

            case "MENU_REVIEWS" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Отзывы</b>\n\n" +
                                    "Мы очень внимательно относимся к обратной связи - она помогает нашему проекту расти! " +
                                    "Оставить свой отзыв о нашей работе и продуктах можно здесь: t.me/kidsriot_feedback",
                            keyboardFactory.getBackButton("BACK_TO_MAIN"))
            );

            case "MENU_CONTACT" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Связь</b>\n\n" +
                                    "Выбери раздел:",
                            keyboardFactory.getContactMenu())
            );

            case "CONTACT_SUPPORT" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Поддержка</b>\n\n" +
                                    "Наш аккаунт поддержки - @kidsriot_support.\n\n" +
                                    "Мы всегда готовы помочь нашим покупателям... Пожалуйста, не стесняйтесь задавать любые вопросы: " +
                                    "выбор размера, доставка, цены - все решим и разрулим ;)\n\n" +
                                    "График работы - с 10:00 до 19:00 в будние дни (но обычно мы онлайн даже ночью 🥲)",
                            keyboardFactory.getSupportContactMenu())
            );

            case "CONTACT_PROPOSALS" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Предложения</b>\n\n" +
                                    "Мы открыты к взаимовыгодному сотрудничеству и всегда находимся в поиске талантливых людей / новых партнеров.\n\n" +
                                    "Пожалуйста, направляйте свои предложения нашему менеджеру (поддержка отвечает ТОЛЬКО покупателям): @janni_rodari",
                            keyboardFactory.getProposalsContactMenu())
            );

            case "PAYMENT" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Проблема с оплатой</b>\n\n" +
                                    "Не проходит оплата / не открывается окно оплаты:\n\n" +
                                    "У тебя случайно не включен VPN? Если да, тогда, отключи его и попробуй заново оформить заказ.\n" +
                                    "Всё равно не проходит оплата? Напиши в поддержку: @kidsriot_support, будем разбираться в чём дело.",
                            keyboardFactory.getBackWithOperatorButton("MENU_PAYMENT"))
            );

            case "PAYMENT2" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Проблема с оплатой долями</b>\n\n" +
                                    "Банк не одобрит покупку Долями, если тебе ещё нет 18. В таком случае можно попросить " +
                                    "кого-то из совершеннолетних друзей, родителей или знакомых оплатить заказ со своей карты.\n\n" +
                                    "Уже есть 18, но Долями всё равно не получается оплатить? " +
                                    "Убедись, что VPN выключен и попробуй заново оформить заказ. Должно помочь. " +
                                    "Если нет, тогда напиши в поддержку: @kidsriot_support. Иногда сервис лагает, и мы подскажем, что делать.",
                            keyboardFactory.getBackWithOperatorButton("MENU_PAYMENT"))
            );

            case "DELIVERY" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Отслеживание</b>\n\n" +
                                    "После отправки мы пришлём тебе трек-номер на указанную при оформлении заказа эл. почту " +
                                    "(или в Telegram, если мы уже на связи в поддержке: @kidsriot_support), " +
                                    "по которому заказ можно будет отслеживать на сайте или в приложении СДЭК / Почта России.\n\n" +
                                    "Если закажешь до 20:00 в будни и до 19:00 в выходные дни - отправим в тот же день. " +
                                    "Если позже - уже на следующий.",
                            keyboardFactory.getBackButton("MENU_DELIVERY"))
            );

            case "DELIVERY_FRONTIER" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Доставка в другие страны</b>\n\n" +
                                    "Мы отправляем заказы и за пределы России… Например: Казахстан, Беларусь, Кыргызстан, " +
                                    "Армения, Узбекистан, Азербайджан, Молдова.\n\n" +
                                    "Для отправки в другую страну, напиши в поддержку: @kidsriot_support. " +
                                    "Найдём удобный для всех способ!",
                            keyboardFactory.getBackWithOperatorButton("MENU_DELIVERY"))
            );

            case "WHEN_DELIVERY" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Сроки</b>\n\n" +
                                    "Мы не любим тянуть с отправками, поэтому они осуществляются каждый день. " +
                                    "Без выходных и отговорок.\n\n" +
                                    "Если закажешь до 20:00 в будни и до 19:00 в выходные дни - отправим в тот же день. " +
                                    "Если позже - уже на следующий.\n\n" +
                                    "Важно учитывать: в праздничные дни отправка зависит только от работы СДЭК и Почты России. " +
                                    "За подробностями пиши в поддержку: @kidsriot_support.\n\n" +
                                    "Кстати, курьером по Москве можем и в праздники доставить, вообще в любое время.",
                            keyboardFactory.getBackWithOperatorButton("MENU_DELIVERY"))
            );

            case "COURIER" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Доставка курьером</b>\n\n" +
                                    "После оплаты заказа наш менеджер связывается с покупателем в мессенджерах " +
                                    "или по телефону и узнает удобное для доставки время, а также другую нужную курьеру информацию.",
                            keyboardFactory.getBackButton("MENU_DELIVERY"))
            );

            case "CLOTHE_BACK" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>Возврат</b>\n\n" +
                                    "Заявления на возврат принимаются в поддержке: @kidsriot_support.\n\n" +
                                    "Важно! Пожалуйста, убедись:\n\n" +
                                    "- Вещь сохранила свой первоначальный вид\n" +
                                    "- С момента получения товара прошло менее 14 дней\n" +
                                    "- Это не шапка или другие аксессуары, которые мы не сможем принять обратно по закону\n\n" +
                                    "Обратную доставку оплачивает покупатель. После получения товара, мы проверяем его " +
                                    "и если всё ок, сразу же оформляем возврат. А дальше уже зависит от банка: " +
                                    "обычно 3 рабочих дня и сумма (без учета доставки) возвращается на счёт.",
                            keyboardFactory.getBackWithOperatorButton("BACK_TO_MAIN"))
            );

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

            default -> messages.add(
                    messageService.createTextMessage(chatId, "❓ Неизвестная команда.", false)
            );
        }
        return messages;
    }
}