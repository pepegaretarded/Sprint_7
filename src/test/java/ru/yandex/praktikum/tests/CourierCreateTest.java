package ru.yandex.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.data.CourierData;
import ru.yandex.praktikum.data.CourierModel;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;

public class CourierCreateTest {
    private CourierModel courier;

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
    @DisplayName("Создать курьера с обязательными полями")
    @Description("Проверить создание курьера с логином, паролем и именем")
    public void shouldCreateCourier() {
        Response response = CourierClient.createCourier(courier);

        assertEquals(SC_CREATED, response.statusCode());
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Не создавать двух курьеров с одинаковым логином")
    @Description("Проверить ошибку при повторном создании курьера с тем же логином")
    public void shouldNotCreateCourierWithExistingLogin() {
        Response firstResponse = CourierClient.createCourier(courier);
        assertEquals(SC_CREATED, firstResponse.statusCode());

        Response duplicateResponse = CourierClient.createCourier(courier);
        assertEquals(SC_CONFLICT, duplicateResponse.statusCode());
        duplicateResponse.then().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Не создавать курьера без логина")
    @Description("Проверить ошибку, если при создании курьера не передан логин")
    public void shouldReturnErrorWhenLoginIsMissing() {
        CourierModel courierWithoutLogin = new CourierModel(null, courier.getPassword(), courier.getFirstName());

        Response response = CourierClient.createCourier(courierWithoutLogin);

        assertEquals(SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Не создавать курьера без пароля")
    @Description("Проверить ошибку, если при создании курьера не передан пароль")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        CourierModel courierWithoutPassword = new CourierModel(courier.getLogin(), null, courier.getFirstName());

        Response response = CourierClient.createCourier(courierWithoutPassword);

        assertEquals(SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
