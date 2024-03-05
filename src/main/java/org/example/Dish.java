package org.example;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dish {
    private int id;
    private String name;
    private int price;
    private String description;
    private long timeToCook;
    private int quantity;

    public Dish(int id, String name, int price, String description, long timeToCook) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.timeToCook = timeToCook;
    }

    public Dish(String name, int price, String description, long timeToCook) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.timeToCook = timeToCook;
    }

}
