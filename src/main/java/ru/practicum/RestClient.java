package ru.practicum;

import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestClient {
    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    private static final Header BASE_CONTENT_TYPE = new Header("Content-type", "application/json");

    public RequestSpecification getBaseSpec() {
        return given()
                .baseUri(BASE_URL)
                .header(BASE_CONTENT_TYPE);
    }

}
