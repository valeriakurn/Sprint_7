package ru.practicum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class LoginCourierTest {
    private static Courier courier;
    private static Courier nonExistentCourier;
    private static Courier courierWithoutLogin;
    private static Courier courierWithoutPassword;
    private static Courier courierWithInvalidLogin;
    private static Courier courierWithInvalidPassword;
    private static CourierClient courierClient;
    private static int courierId;

    @BeforeClass
    public static void setUp() {
        courier = CourierGenerator.getDefaultCourier();
        nonExistentCourier = CourierGenerator.getNonExistentCourier();
        courierWithoutLogin = CourierGenerator.getCourierWithoutLogin();
        courierWithoutPassword = CourierGenerator.getCourierWithoutPassword();
        courierWithInvalidLogin = CourierGenerator.getCourierWithInvalidLogin();
        courierWithInvalidPassword = CourierGenerator.getCourierWithInvalidPassword();
        courierClient = new CourierClient();

        courierClient.create(courier);
    }

    @Test
    @DisplayName("Courier can be logged in")
    @Description("Verify that in case courier is successfully logged in, expected status code and message will be received")
    public void CourierCanBeLoggedInTest() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, loginStatusCode);

        courierId = loginResponse.extract().path("id");
        assertNotNull("Login was not successful", courierId);
    }

    @Test
    @DisplayName("Courier cannot be logged in without login")
    @Description("Verify that in case courier is tried to log in without login, expected status code and error message will be received")
    public void CourierCannotBeLoggedInWithoutLoginTest() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierWithoutLogin));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, loginStatusCode);

        String errorMessage = loginResponse.extract().path("message");
        assertEquals("Incorrect error message was received", "Недостаточно данных для входа", errorMessage);
    }

    @Test
    @DisplayName("Courier cannot be logged in without password")
    @Description("Verify that in case courier is tried to log in without password, expected status code and error message will be received")
    public void CourierCannotBeLoggedInWithoutPasswordTest() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierWithoutPassword));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, loginStatusCode);

        String errorMessage = loginResponse.extract().path("message");
        assertEquals("Incorrect error message was received", "Недостаточно данных для входа", errorMessage);
    }

    @Test
    @DisplayName("Non-existent courier cannot be logged in")
    @Description("Verify that in case non-existent courier is tried to log in, expected status code and error message will be received")
    public void NonExistentCourierCannotBeLoggedInTest() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(nonExistentCourier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_NOT_FOUND, loginStatusCode);

        String errorMessage = loginResponse.extract().path("message");
        assertEquals("Incorrect error message was received", "Учетная запись не найдена", errorMessage);
    }

    @Test
    @DisplayName("Courier cannot be logged in with invalid login")
    @Description("Verify that in case courier is tried to log in with invalid login, expected status code and error message will be received")
    public void CourierCannotBeLoggedInWithInvalidLoginTest() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierWithInvalidLogin));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_NOT_FOUND, loginStatusCode);

        String errorMessage = loginResponse.extract().path("message");
        assertEquals("Incorrect error message was received", "Учетная запись не найдена", errorMessage);
    }

    @Test
    @DisplayName("Courier cannot be logged in with invalid password")
    @Description("Verify that in case courier is tried to log in with invalid password, expected status code and error message will be received")
    public void CourierCannotBeLoggedInWithInvalidPasswordTest() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierWithInvalidPassword));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_NOT_FOUND, loginStatusCode);

        String errorMessage = loginResponse.extract().path("message");
        assertEquals("Incorrect error message was received", "Учетная запись не найдена", errorMessage);
    }

    @AfterClass
    public static void tearDown() {
        courierClient.delete(courierId);
    }
}
