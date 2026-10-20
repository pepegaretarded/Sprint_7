package ru.yandex.praktikum.tests;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.data.CourierData;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CourierCreateTest {
    private CourierData courier;

    @Before
    @Step("Подготовить данные курьера")
    public void setUp() {
        courier = CourierData.random();
    }

    @After
    @Step("Удалить созданного в тесте курьера")
    public void tearDown() {
        CourierClient.deleteCourierIfCreated(courier);
    }

    @Test
    @Step("Создать курьера с обязательными полями")
    public void shouldCreateCourier() {
        Response response = CourierClient.createCourier(courier.toMap());

        assertEquals(201, response.statusCode());
        response.then().body("ok", equalTo(true));
    }

    @Test
    @Step("Не создавать двух курьеров с одинаковым логином")
    public void shouldNotCreateCourierWithExistingLogin() {
        Response firstResponse = CourierClient.createCourier(courier.toMap());
        assertEquals(201, firstResponse.statusCode());

        Response duplicateResponse = CourierClient.createCourier(courier.toMap());
        assertEquals(409, duplicateResponse.statusCode());
        duplicateResponse.then().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @Step("Не создавать курьера без логина")
    public void shouldReturnErrorWhenLoginIsMissing() {
        Map<String, Object> courierWithoutLogin = new HashMap<>(courier.toMap());
        courierWithoutLogin.remove("login");

        Response response = CourierClient.createCourier(courierWithoutLogin);

        assertEquals(400, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Step("Не создавать курьера без пароля")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        Map<String, Object> courierWithoutPassword = new HashMap<>(courier.toMap());
        courierWithoutPassword.remove("password");

        Response response = CourierClient.createCourier(courierWithoutPassword);

        assertEquals(400, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
