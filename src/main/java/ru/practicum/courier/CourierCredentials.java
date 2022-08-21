package ru.practicum.courier;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CourierCredentials {
    private String login;
    private String password;

    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(courier.getLogin(), courier.getPassword());
    }
}

