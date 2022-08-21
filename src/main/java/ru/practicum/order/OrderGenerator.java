package ru.practicum.order;

public class OrderGenerator {

    public static Order getDefaultOrder(String[] color) {
        return new Order(
                "Ivan",
                "Ivanov",
                "Tverskaya 12, apt.46",
                "Tverskaya",
                "+ 7 999 99 99",
                10,
                "2020-06-06",
                "Fully charged",
                color);
    }
}
