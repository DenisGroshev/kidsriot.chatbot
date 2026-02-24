package com.gmail.denigroshev.kidsriot.chatbot.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PromoCodeService {

    public record PromoCode(String code, String discount) {}

    private final List<PromoCode> availableCodes = new ArrayList<>(List.of(
            new PromoCode("04KMOA", "30%"),
            new PromoCode("7K9ZT5", "20%"),
            new PromoCode("3BIJPU", "30%"),
            new PromoCode("EA1MLX", "20%"),
            new PromoCode("RB10KE", "40%"),
            new PromoCode("BZYU3X", "30%"),
            new PromoCode("VS8OV5", "20%"),
            new PromoCode("T6WZGS", "20%"),
            new PromoCode("L4SH1E", "30%"),
            new PromoCode("KJC7QP", "30%"),
            new PromoCode("T6PI03", "20%"),
            new PromoCode("U5F7MK", "40%"),
            new PromoCode("2907VU", "30%"),
            new PromoCode("5URHOY", "20%"),
            new PromoCode("3NWH9Y", "20%"),
            new PromoCode("KM7CJA", "40%"),
            new PromoCode("AOGC9P", "30%"),
            new PromoCode("JIW324", "40%"),
            new PromoCode("L70TY0", "20%"),
            new PromoCode("C1MCBS", "20%"),
            new PromoCode("2Y15RM", "30%"),
            new PromoCode("B893SX", "30%"),
            new PromoCode("13M64F", "20%"),
            new PromoCode("C0SXON", "20%"),
            new PromoCode("V5EJ37", "30%"),
            new PromoCode("14OCU8", "40%"),
            new PromoCode("A1AWKX", "30%"),
            new PromoCode("A2MGDG", "20%"),
            new PromoCode("1ALD7M", "20%"),
            new PromoCode("3A5B0R", "30%"),
            new PromoCode("RED3BX", "20%"),
            new PromoCode("NIUN9N", "40%"),
            new PromoCode("UM2UEA", "20%"),
            new PromoCode("1M5YOT", "20%"),
            new PromoCode("WTQE07", "30%"),
            new PromoCode("CUEE23", "30%"),
            new PromoCode("F6WESV", "40%"),
            new PromoCode("MCP4TM", "50%"),
            new PromoCode("CQTAA7", "30%"),
            new PromoCode("21A9BP", "20%"),
            new PromoCode("FKS5TP", "20%"),
            new PromoCode("HM344A", "30%"),
            new PromoCode("4CTORO", "20%"),
            new PromoCode("6RRPJO", "30%"),
            new PromoCode("I2OPXV", "40%"),
            new PromoCode("EMI47A", "20%"),
            new PromoCode("6OIC7N", "30%"),
            new PromoCode("A6VOVZ", "20%"),
            new PromoCode("G8624J", "20%"),
            new PromoCode("NC5B0J", "30%"),
            new PromoCode("AKBJZ2", "40%"),
            new PromoCode("P7QVCK", "20%"),
            new PromoCode("465MPG", "20%"),
            new PromoCode("WSU0LQ", "30%"),
            new PromoCode("D0VKZA", "40%"),
            new PromoCode("3TO5XS", "20%"),
            new PromoCode("YC60VA", "20%"),
            new PromoCode("IK8AQ0", "30%"),
            new PromoCode("VJG7ZZ", "20%"),
            new PromoCode("UK9PK5", "50%"),
            new PromoCode("KEAX61", "20%"),
            new PromoCode("0M6EJG", "20%"),
            new PromoCode("BTYXC5", "30%"),
            new PromoCode("2YG6TH", "20%"),
            new PromoCode("A89Y7O", "20%"),
            new PromoCode("77TOHR", "30%"),
            new PromoCode("2MOTSB", "40%"),
            new PromoCode("LIC2PX", "20%"),
            new PromoCode("ZP71D4", "20%"),
            new PromoCode("AE9BJX", "20%"),
            new PromoCode("060QC2", "30%"),
            new PromoCode("AHKR3M", "40%"),
            new PromoCode("03Y4RH", "30%"),
            new PromoCode("KXQ2GT", "20%"),
            new PromoCode("IIS0TU", "30%"),
            new PromoCode("1NTUHJ", "20%"),
            new PromoCode("3ME4YF", "20%"),
            new PromoCode("EWMC4I", "30%"),
            new PromoCode("R34NQD", "20%"),
            new PromoCode("H4XD95", "20%"),
            new PromoCode("NZYB3A", "30%"),
            new PromoCode("YL2QLX", "40%"),
            new PromoCode("SB9LWU", "20%"),
            new PromoCode("O5UVFG", "30%"),
            new PromoCode("FKK2P7", "20%"),
            new PromoCode("37JESC", "30%"),
            new PromoCode("KXE59M", "20%"),
            new PromoCode("X8RPHK", "20%"),
            new PromoCode("GNPXV8", "20%"),
            new PromoCode("SJ9H1J", "40%"),
            new PromoCode("S6S74D", "20%"),
            new PromoCode("7JAKS4", "50%"),
            new PromoCode("ZEF7PB", "30%"),
            new PromoCode("94EGB0", "30%"),
            new PromoCode("3T1IE3", "20%"),
            new PromoCode("1WIB3Z", "20%"),
            new PromoCode("EONU6T", "30%"),
            new PromoCode("K8ZBUC", "20%"),
            new PromoCode("AYNLA7", "40%"),
            new PromoCode("M9SL2T", "50%")
    ));

    // chatId -> PromoCode (каждый пользователь получает свой зафиксированный промокод)
    private final Map<Long, PromoCode> issuedCodes = new ConcurrentHashMap<>();

    public PromoResult requestPromoCode(Long chatId) {
        // Уже получал — возвращаем тот же
        PromoCode existing = issuedCodes.get(chatId);
        if (existing != null) {
            return PromoResult.alreadyIssued(existing);
        }

        // Выдаём рандомный из списка (не удаляем)
        int index = (int) (Math.random() * availableCodes.size());
        PromoCode promoCode = availableCodes.get(index);
        issuedCodes.put(chatId, promoCode);
        return PromoResult.issued(promoCode);
    }

    public static class PromoResult {
        public enum Status {
            ISSUED,
            ALREADY_ISSUED,
            EXHAUSTED
        }

        private final Status status;
        private final PromoCode promoCode;

        private PromoResult(Status status, PromoCode promoCode) {
            this.status = status;
            this.promoCode = promoCode;
        }

        public static PromoResult issued(PromoCode promoCode) {
            return new PromoResult(Status.ISSUED, promoCode);
        }

        public static PromoResult alreadyIssued(PromoCode promoCode) {
            return new PromoResult(Status.ALREADY_ISSUED, promoCode);
        }

        public static PromoResult exhausted() {
            return new PromoResult(Status.EXHAUSTED, null);
        }

        public Status getStatus() { return status; }

        public String getCode() {
            return promoCode != null ? promoCode.code() : null;
        }

        public String getDiscount() {
            return promoCode != null ? promoCode.discount() : null;
        }
    }
}