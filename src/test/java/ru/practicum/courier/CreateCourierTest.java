package ru.practicum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateCourierTest {
    private static Courier courier;
    private static Courier courierWithNotUniqueLogin;
    private static Courier courierWithoutLogin;
    private static Courier courierWithoutPassword;
    private static CourierClient courierClient;
    private int courierId;

    @BeforeClass
    public static void setUp() {
        courier = CourierGenerator.getDefaultCourier();
        courierWithNotUniqueLogin = CourierGenerator.getCourierWithDefaultLogin();
        courierWithoutLogin = CourierGenerator.getCourierWithoutLogin();
        courierWithoutPassword = CourierGenerator.getCourierWithoutPassword();
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Courier can be created")
    @Description("Verify that in case courier is successfully created, expected status code and message will be received")
    public void CourierCanBeCreatedTest() {
        ValidatableResponse response = courierClient.create(courier);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_CREATED, statusCode);

        boolean isCreated = response.extract().path("ok");
        assertTrue("Courier was not created", isCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, loginStatusCode);

        courierId = loginResponse.extract().path("id");
        assertNotNull("Login was not successful", courierId);
    }

    @Test
    @DisplayName("Two identical couriers cannot be created")
    @Description("Verify that in case two identical couriers are tried to be created, expected status code and error message will be received")
    public void TwoIdenticalCouriersCannotBeCreatedTest() {
        courierClient.create(courier);
        ValidatableResponse response = courierClient.create(courier);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_CONFLICT, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "Этот логин уже используется", errorMessage);
    }

    @Test
    @DisplayName("Two couriers with the same login cannot be created")
    @Description("Verify that in case two couriers are tried to be created using same login, expected status code and error message will be received")
    public void TwoCouriersWithSameLoginCannotBeCreatedTest() {
        courierClient.create(courier);
        ValidatableResponse response = courierClient.create(courierWithNotUniqueLogin);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_CONFLICT, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "Этот логин уже используется", errorMessage);
    }

    @Test
    @DisplayName("Courier cannot be created without login")
    @Description("Verify that in case courier is tried to be created without login, expected status code and error message will be received")
    public void CreateCourierWithoutLoginTest() {
        ValidatableResponse response = courierClient.create(courierWithoutLogin);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "Недостаточно данных для создания учетной записи", errorMessage);
    }

    @Test
    @DisplayName("Courier cannot be created without password")
    @Description("Verify that in case courier is tried to be created without password, expected status code and error message will be received")
    public void CreateCourierWithoutPasswordTest() {
        ValidatableResponse response = courierClient.create(courierWithoutPassword);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "Недостаточно данных для создания учетной записи", errorMessage);
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

}
