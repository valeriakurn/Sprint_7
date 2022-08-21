package ru.practicum.courier;

public class CourierGenerator {

    public static Courier getDefaultCourier() {
        return new Courier("ivan_ivanov", "Test1234", "Ivan");
    }

    public static Courier getCourierWithDefaultLogin() {
        return new Courier("ivan_ivanov", "Test4321", "Petr");
    }

    public static Courier getNonExistentCourier() {
        return new Courier("oleg_petrov", "Test1234", "Oleg");
    }

    public static Courier getCourierWithoutLogin() {
        return new Courier(null, "Test1234", "Ivan");
    }

    public static Courier getCourierWithoutPassword() {
        return new Courier("ivan_ivanov", null, "Ivan");
    }

    public static Courier getCourierWithInvalidLogin() {
        return new Courier("ivan123", "Test1234", "Ivan");
    }

    public static Courier getCourierWithInvalidPassword() {
        return new Courier("ivan1234", "Test123", "Ivan");
    }
}
