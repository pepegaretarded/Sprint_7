package ru.yandex.praktikum.tests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.data.OrderData;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final String colorName;
    private final String[] colors;

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

    @Test
    @Step("Создать заказ с выбранными цветами")
    public void shouldCreateOrderWithSelectedColors() {
        Response response = OrderClient.createOrder(OrderData.withColors(colors));
        Object trackValue = response.jsonPath().get("track");
        try {
            assertEquals(201, response.statusCode());
            response.then().body("track", notNullValue());
        } finally {
            if (trackValue != null) {
                int track = Integer.parseInt(trackValue.toString());
                Response cancelResponse = OrderClient.cancelOrder(track);
                assertEquals(200, cancelResponse.statusCode());
                cancelResponse.then().body("ok", org.hamcrest.Matchers.equalTo(true));
            }
        }
    }
}
