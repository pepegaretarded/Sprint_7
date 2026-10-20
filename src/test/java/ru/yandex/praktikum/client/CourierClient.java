package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.config.ApiConfig;
import ru.yandex.praktikum.data.CourierData;

import java.util.Map;

public final class CourierClient {
    private CourierClient() {
    }

    @Step("Создать курьера")
    public static Response createCourier(Map<String, Object> courier) {
        return ApiClient.request().body(courier).post(ApiConfig.COURIER_PATH);
    }

    @Step("Войти в систему под курьером")
    public static Response loginCourier(Map<String, Object> credentials) {
        return ApiClient.request().body(credentials).post(ApiConfig.COURIER_LOGIN_PATH);
    }

    @Step("Удалить курьера, если он был создан")
    public static void deleteCourierIfCreated(CourierData courier) {
        Response login = loginCourier(courier.toMap());
        if (login.statusCode() == 200) {
            int courierId = login.jsonPath().getInt("id");
            ApiClient.request().delete(ApiConfig.COURIER_PATH + "/" + courierId);
        }
    }
}
