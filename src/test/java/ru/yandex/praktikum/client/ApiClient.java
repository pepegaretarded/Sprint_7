package ru.yandex.praktikum.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.yandex.praktikum.config.ApiConfig;

public final class ApiClient {
    private ApiClient() {
    }

    public static RequestSpecification request() {
        return RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured());
    }
}
