package org.example;

import lombok.Getter;
import org.example.model.Order;
import org.example.model.OrderStatus;
import java.util.List;

public class OrderRepo {
    List<Order> orders;

    @Getter
    int orderSessionCounter = 0;

    public OrderRepo() {
        orders = DataBaseHandler.loadOrders();
    }

    public void addOrder(Order activeOrder) {
        orderSessionCounter++;
        DataBaseHandler.addOrder(activeOrder);
        update();
    }

    public void updateOrder(Order activeOrder) {
        DataBaseHandler.updateOrder(activeOrder);
        update();
    }

    public Order getActiveOrderByUserId(int id) {
        return DataBaseHandler.loadOrders().stream().filter(order -> order.getUserId() == id)
                .filter(order -> order.getStatus() != OrderStatus.PAID).findFirst().orElse(null);
    }

    public void update() {
        orders = DataBaseHandler.loadOrders();
    }

    public void removeOrder(Order order) {
        DataBaseHandler.removeOrder(order);
        update();
    }

    public List<Order> getOrders() {
        return orders;
    }
}
