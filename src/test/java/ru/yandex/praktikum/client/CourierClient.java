package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import com.google.gson.Gson;
import io.restassured.response.Response;
import ru.yandex.praktikum.config.ApiConfig;
import ru.yandex.praktikum.data.CourierModel;

public final class CourierClient {
    private static final Gson GSON = new Gson();

    private CourierClient() {
    }

    @Step("Создать курьера")
    public static Response createCourier(CourierModel courier) {
        return ApiClient.request().body(GSON.toJson(courier)).post(ApiConfig.COURIER_PATH);
    }

    @Step("Войти в систему под курьером")
    public static Response loginCourier(CourierModel credentials) {
        return ApiClient.request().body(GSON.toJson(credentials)).post(ApiConfig.COURIER_LOGIN_PATH);
    }

    @Step("Удалить курьера, если он был создан")
    public static void deleteCourierIfCreated(CourierModel courier) {
        Response login = loginCourier(courier);
        if (login.statusCode() == org.apache.http.HttpStatus.SC_OK) {
            int courierId = login.jsonPath().getInt("id");
            ApiClient.request().delete(ApiConfig.COURIER_PATH + "/" + courierId);
        }
    }
}
