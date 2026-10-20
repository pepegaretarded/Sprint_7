package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.config.ApiConfig;

import java.util.Map;

public final class OrderClient {
    private OrderClient() {
    }

    @Step("Создать заказ")
    public static Response createOrder(Map<String, Object> order) {
        return ApiClient.request().body(order).post(ApiConfig.ORDERS_PATH);
    }

    @Step("Отменить заказ по треку {track}")
    public static Response cancelOrder(int track) {
        return ApiClient.request()
                .queryParam("track", track)
                .put(ApiConfig.ORDERS_CANCEL_PATH);
    }

    @Step("Получить список заказов")
    public static Response getOrders() {
        return ApiClient.request().get(ApiConfig.ORDERS_PATH);
    }
}
