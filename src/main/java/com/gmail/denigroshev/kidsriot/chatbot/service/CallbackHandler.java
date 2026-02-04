package com.gmail.denigroshev.kidsriot.chatbot.service;

import com.gmail.denigroshev.kidsriot.chatbot.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackHandler {

    private final TelegramMessageService messageService;
    private final KeyboardFactory keyboardFactory;

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

            case "MENU_ABOUT" -> messages.add(
                    messageService.editMessage(chatId, messageId,
                            "<b>О проекте. Манифест</b>\n\n" +
                                    "Kids Riot появился в 2025 году с целью создать что-то абсолютно новое " +
                                    "без оглядки на надоевшие тренды и правила. Мы делаем стритвир с характером, " +
                                    "берём привычные формы и наполняем их смыслом, иронией и бунтом. " +
                                    "Без безликих решений и пустого копирования. Каждая вещь - это вызов скучной «взрослой» жизни.\n\n" +
                                    "Наш бренд создан для молодых, думающих и креативных ребят, " +
                                    "кому важно отличаться и иногда идти против течения.\n\n" +
                                    "Мы создаём своё комьюнити для тех, кому откликаются наши идеи. " +
                                    "Если тебе близок такой подход, значит, мы на одной стороне!",
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

            // Оплата
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

            // Доставка
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

            // Возврат
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

            default -> messages.add(
                    messageService.createTextMessage(chatId, "❓ Неизвестная команда.", false)
            );
        }
        return messages;
    }
}
