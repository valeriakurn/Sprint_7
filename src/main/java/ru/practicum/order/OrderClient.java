package ru.practicum.order;

import io.restassured.response.ValidatableResponse;
import ru.practicum.RestClient;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDER_PATH = "/api/v1/orders";
    private static final String GET_ORDER_PATH = "/api/v1/orders/track";

    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse getOneOrder(int track) {
        return given()
                .spec(getBaseSpec())
                .queryParam("t", track)
                .get(GET_ORDER_PATH)
                .then();
    }

    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH)
                .then();
    }
}
