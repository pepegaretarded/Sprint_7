package ru.yandex.praktikum.data;

import java.util.UUID;

public final class CourierData {
    public static CourierModel random() {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        return new CourierModel("courier" + suffix, "pass" + suffix, "Алексей");
    }
}
