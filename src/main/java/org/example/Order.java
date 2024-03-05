package org.example;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private OrderStatus status;
    List<Dish> dishes;

    public Order() {
        dishes = new ArrayList<>();
        status = OrderStatus.NEW;
    }

    public void addDish(Dish dish, int quantity) {
        for (int i = 0; i < quantity; i++) {
            dishes.add(dish);
        }
    }

    public void removeDish(Dish dish) {
        dishes.remove(dish);
    }

}
