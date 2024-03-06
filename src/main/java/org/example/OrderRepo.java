package org.example;

import org.example.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepo {
    List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }


}
