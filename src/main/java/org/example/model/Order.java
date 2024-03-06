package org.example.model;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class Order {
    private int id;
    private OrderStatus status;

    List<Dish> dishes;

    public Order() {
        dishes = new ArrayList<>();
        status = OrderStatus.NEW;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

    public void removeDish(Dish dish) {
        dishes.remove(dish);
    }

}
