package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.data.OrderData;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final String colorName;
    private final String[] colors;
    private Integer track;

    public OrderCreateTest(String colorName, String[] colors) {
        this.colorName = colorName;
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Collection<Object[]> orderColors() {
        return Arrays.asList(new Object[][]{
                {"BLACK", new String[]{"BLACK"}},
                {"GREY", new String[]{"GREY"}},
                {"BLACK и GREY", new String[]{"BLACK", "GREY"}},
                {"не указан", null}
        });
    }

    @After
    @Step("Удалить созданный заказ")
    public void tearDown() {
        if (track != null) {
            Response response = OrderClient.cancelOrder(track);
            assertEquals(SC_OK, response.statusCode());
            response.then().body("ok", org.hamcrest.Matchers.equalTo(true));
        }
    }

    @Test
    @DisplayName("Создать заказ с выбранным цветом")
    @Description("Проверить создание заказа с выбранным набором цветов")
    public void shouldCreateOrderWithSelectedColors() {
        Response response = OrderClient.createOrder(OrderData.withColors(colors));
        if (response.statusCode() == SC_CREATED && response.jsonPath().get("track") != null) {
            track = response.jsonPath().getInt("track");
        }

        assertEquals(SC_CREATED, response.statusCode());
        response.then().body("track", notNullValue());
    }
}
