package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import com.google.gson.Gson;
import io.restassured.response.Response;
import ru.yandex.praktikum.config.ApiConfig;
import ru.yandex.praktikum.data.OrderModel;

public final class OrderClient {
    private static final Gson GSON = new Gson();

    private OrderClient() {
    }

    @Step("Создать заказ")
    public static Response createOrder(OrderModel order) {
        return ApiClient.request().body(GSON.toJson(order)).post(ApiConfig.ORDERS_PATH);
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
