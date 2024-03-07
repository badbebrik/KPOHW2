package org.example.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;



@Builder
@AllArgsConstructor
@Data
public class Order {
    private int id;
    private OrderStatus status;
    private int userId;

    List<Dish> dishes;

    public Order() {
        dishes = new ArrayList<>();
        status = OrderStatus.NEW;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }

}
