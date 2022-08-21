package ru.practicum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class GetOrdersTest {
    private static OrderClient orderClient;

    @BeforeClass
    public static void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("List of orders can be returned")
    @Description("Verify that in case list of orders is returned, expected status code will be received")
    public void ListOfOrdersCanBeReturnedTest() {
        ValidatableResponse response = orderClient.getAllOrders();

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        ArrayList<String> orders = response.extract().path("orders");
        assertNotNull("List of orders is empty", orders);
        assertNotEquals("List of orders is empty", orders.size(), 0);
    }
}
