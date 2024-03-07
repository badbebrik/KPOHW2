package org.example.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



@Builder
@AllArgsConstructor
@Data
public class Order {
    private int id;
    private OrderStatus status;
    private int userId;
    private boolean isCancelled = false;

    List<Dish> dishes;

    public Order() {
        dishes = new ArrayList<>();
        status = OrderStatus.NEW;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

}
