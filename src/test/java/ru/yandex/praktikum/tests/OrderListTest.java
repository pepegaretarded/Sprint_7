package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderClient;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.apache.http.HttpStatus.SC_OK;

public class OrderListTest {
    @Test
    @DisplayName("Получить список заказов")
    @Description("Проверить, что сервис возвращает список заказов")
    public void shouldReturnListOfOrders() {
        Response response = OrderClient.getOrders();

        assertEquals(SC_OK, response.statusCode());
        List<?> orders = response.jsonPath().getList("orders");
        assertNotNull(orders);
    }
}
