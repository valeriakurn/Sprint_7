package ru.practicum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private Order order;
    private static OrderClient orderClient;
    private final String[] COLOR;

    public CreateOrderTest (String[] color) {
        this.COLOR = color;
    }

    @BeforeClass
    public static void setUp() {
        orderClient = new OrderClient();
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][]{
                {new String[]{"GRAY", "BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK"}},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Order can be created with variety of colors specified")
    @Description("Verify that in case order is successfully created, expected status code and message will be received")
    public void OrderCanBeCreatedTest() {
        order = OrderGenerator.getDefaultOrder(COLOR);
        ValidatableResponse response = orderClient.create(order);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_CREATED, statusCode);

        int track = response.extract().path("track");
        assertNotNull("Order was not created", track);

        ValidatableResponse getOrderResponse = orderClient.getOneOrder(track);
        int getOrderStatusCode = getOrderResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, getOrderStatusCode);

        String order = getOrderResponse.extract().path("order").toString();
        assertNotEquals("List of orders is empty", order, "{}");
    }

}
