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
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class CourierLoginTest {
    private CourierData courier;

    @Before
    @Step("Создать курьера для проверки входа")
    public void setUp() {
        courier = CourierData.random();
        Response response = CourierClient.createCourier(courier.toMap());
        assertEquals(201, response.statusCode());
        response.then().body("ok", equalTo(true));
    }

    @After
    @Step("Удалить тестового курьера")
    public void tearDown() {
        CourierClient.deleteCourierIfCreated(courier);
    }

    @Test
    @Step("Войти с правильными логином и паролем")
    public void shouldLoginCourier() {
        Response response = CourierClient.loginCourier(courier.toMap());

        assertEquals(200, response.statusCode());
        response.then().body("id", notNullValue());
    }

    @Test
    @Step("Вернуть ошибку, если не указан логин")
    public void shouldReturnErrorWhenLoginIsMissing() {
        Map<String, Object> credentials = new HashMap<>(courier.toMap());
        credentials.remove("login");

        Response response = CourierClient.loginCourier(credentials);

        assertEquals(400, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Вернуть ошибку, если не указан пароль")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        Map<String, Object> credentials = new HashMap<>(courier.toMap());
        credentials.remove("password");

        Response response = CourierClient.loginCourier(credentials);

        assertEquals(400, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Вернуть ошибку для неправильного пароля")
    public void shouldReturnErrorWhenPasswordIsIncorrect() {
        Map<String, Object> credentials = new HashMap<>(courier.toMap());
        credentials.put("password", "неверный пароль");

        Response response = CourierClient.loginCourier(credentials);

        assertEquals(404, response.statusCode());
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Step("Вернуть ошибку для несуществующего пользователя")
    public void shouldReturnErrorWhenCourierDoesNotExist() {
        Map<String, Object> credentials = new HashMap<>(courier.toMap());
        credentials.put("login", "нет" + courier.getLogin());

        Response response = CourierClient.loginCourier(credentials);

        assertEquals(404, response.statusCode());
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }
}
