package org.example.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private int id;
    private String name;
    private int price;
    private String description;
    private long timeToCook;
    private int quantity;

    public Dish(String name, String description, int price, int quantity, long cookingTime) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.timeToCook = cookingTime;
    }
}