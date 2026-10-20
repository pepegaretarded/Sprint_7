package ru.yandex.praktikum.tests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderClient;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderListTest {
    @Test
    @Step("Получить список заказов")
    public void shouldReturnListOfOrders() {
        Response response = OrderClient.getOrders();

        assertEquals(200, response.statusCode());
        List<?> orders = response.jsonPath().getList("orders");
        assertNotNull(orders);
    }
}
