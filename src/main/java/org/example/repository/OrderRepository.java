package org.example.repository;

import org.example.model.Order;

public interface OrderRepository {
    void addOrder(Order order);

    int getOrderNumber();

    int getOrderSessionCounter();

    void removeOrder(Order order);

    boolean anyOrderIsInProgressOrNew();

    void update();

    Order getActiveOrderByUserId(int id);

    void updateOrder(Order activeOrder);

}
