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
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

public class CourierLoginTest {
    private CourierModel courier;

    @Before
    @Step("Создать курьера для проверки входа")
    public void setUp() {
        courier = CourierData.random();
        Response response = CourierClient.createCourier(courier);
        assertEquals(SC_CREATED, response.statusCode());
        response.then().body("ok", equalTo(true));
    }

    @After
    @Step("Удалить тестового курьера")
    public void tearDown() {
        CourierClient.deleteCourierIfCreated(courier);
    }

    @Test
    @DisplayName("Войти с правильными логином и паролем")
    @Description("Проверить успешную авторизацию курьера и наличие его идентификатора")
    public void shouldLoginCourier() {
        Response response = CourierClient.loginCourier(courier);

        assertEquals(SC_OK, response.statusCode());
        response.then().body("id", notNullValue());
    }

    @Test
    @DisplayName("Вернуть ошибку, если не указан логин")
    @Description("Проверить ошибку авторизации, если логин не передан")
    public void shouldReturnErrorWhenLoginIsMissing() {
        CourierModel credentials = new CourierModel(null, courier.getPassword(), courier.getFirstName());

        Response response = CourierClient.loginCourier(credentials);

        assertEquals(SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Вернуть ошибку, если не указан пароль")
    @Description("Проверить ошибку авторизации, если пароль не передан")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        CourierModel credentials = new CourierModel(courier.getLogin(), null, courier.getFirstName());

        Response response = CourierClient.loginCourier(credentials);

        assertEquals(SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Вернуть ошибку для неправильного пароля")
    @Description("Проверить ошибку авторизации с неправильным паролем")
    public void shouldReturnErrorWhenPasswordIsIncorrect() {
        CourierModel credentials = new CourierModel(
                courier.getLogin(), "неверный пароль", courier.getFirstName());

        Response response = CourierClient.loginCourier(credentials);

        assertEquals(SC_NOT_FOUND, response.statusCode());
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Вернуть ошибку для несуществующего пользователя")
    @Description("Проверить ошибку авторизации для курьера с несуществующим логином")
    public void shouldReturnErrorWhenCourierDoesNotExist() {
        CourierModel credentials = new CourierModel(
                "нет" + courier.getLogin(), courier.getPassword(), courier.getFirstName());

        Response response = CourierClient.loginCourier(credentials);

        assertEquals(SC_NOT_FOUND, response.statusCode());
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }
}
