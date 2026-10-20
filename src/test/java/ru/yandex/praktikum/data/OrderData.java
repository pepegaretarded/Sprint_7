package ru.yandex.praktikum.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public final class OrderData {
    private OrderData() {
    }

    public static Map<String, Object> withColors(String[] colors) {
        Map<String, Object> order = new HashMap<>();
        order.put("firstName", "Алексей");
        order.put("lastName", "Иванов");
        order.put("address", "Москва, улица Ленина, 1");
        order.put("metroStation", "4");
        order.put("phone", "+7 800 355 35 35");
        order.put("rentTime", 2);
        order.put("deliveryDate", LocalDate.now().plusDays(1).toString());
        order.put("comment", "Проверка заказа");
        if (colors != null) {
            order.put("color", colors);
        }
        return order;
    }
}
