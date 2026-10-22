package ru.yandex.praktikum.data;

import java.time.LocalDate;
public final class OrderData {
    private OrderData() {
    }

    public static OrderModel withColors(String[] colors) {
        return new OrderModel(
                "Алексей",
                "Иванов",
                "Москва, улица Ленина, 1",
                "4",
                "+7 800 355 35 35",
                2,
                LocalDate.now().plusDays(1).toString(),
                "Проверка заказа",
                colors
        );
    }
}
