package org.example.repository;

import lombok.Getter;
import org.example.database.DataBaseHandler;
import org.example.model.Order;
import org.example.model.OrderStatus;

import java.util.Iterator;
import java.util.List;

public class OrderRepositoryImpl implements Iterable<Order>, OrderRepository {
    List<Order> orders;

    @Getter
    int orderSessionCounter = 0;

    public OrderRepositoryImpl() {
        orders = DataBaseHandler.loadOrders();
    }

    @Override
    public void addOrder(Order activeOrder) {
        orderSessionCounter++;
        DataBaseHandler.addOrder(activeOrder);
        update();
    }

    @Override
    public void updateOrder(Order activeOrder) {
        DataBaseHandler.updateOrder(activeOrder);
        update();
    }

    @Override
    public Order getActiveOrderByUserId(int id) {
        return DataBaseHandler.loadOrders().stream().filter(order -> order.getUserId() == id)
                .filter(order -> order.getStatus() != OrderStatus.PAID).findFirst().orElse(null);
    }

    @Override
    public void update() {
        orders = DataBaseHandler.loadOrders();
    }

    @Override
    public void removeOrder(Order order) {
        DataBaseHandler.removeOrder(order);
        update();
    }

    @Override
    public Iterator<Order> iterator() {
        return new OrderIterator();
    }

    @Override
    public boolean anyOrderIsInProgressOrNew() {
        return orders.stream().anyMatch(order -> order.getStatus() == OrderStatus.IN_PROGRESS || order.getStatus() == OrderStatus.NEW);
    }

    private class OrderIterator implements Iterator<Order> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < orders.size();
        }

        @Override
        public Order next() {
            return orders.get(currentIndex++);
        }
    }

    @Override
    public int getOrderNumber() {
        return orders.size();
    }
}
