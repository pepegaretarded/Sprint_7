package ru.yandex.praktikum.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CourierData {
    private final String login;
    private final String password;
    private final String firstName;

    public CourierData(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public static CourierData random() {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        return new CourierData("courier" + suffix, "pass" + suffix, "Алексей");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> courier = new HashMap<>();
        courier.put("login", login);
        courier.put("password", password);
        courier.put("firstName", firstName);
        return courier;
    }

    public String getLogin() {
        return login;
    }
}
