package org.example.controller;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.example.model.Dish;
import org.example.model.Order;
import org.example.model.OrderStatus;


import java.util.concurrent.*;

public class Kitchen {
    private final ThreadPoolExecutor chefPool;
    private final LinkedBlockingQueue<Order> orderQueue;

    public Kitchen(int numberOfChefs) {
        orderQueue = new LinkedBlockingQueue<>();
        chefPool = new ThreadPoolExecutor(numberOfChefs, numberOfChefs, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        startProcessingOrders();
    }

    public void addOrder(Order order, Runnable callback) {
        orderQueue.add(order);
    }

    private void startProcessingOrders() {
        Runnable task = () -> {
            while (true) {
                try {
                    Order order = orderQueue.take(); // Блокирующая операция - ждем заказа
                    if (!order.getDishes().isEmpty()) {
                        chefPool.execute(() -> processOrder(order));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        chefPool.execute(task);
    }

    private void processOrder(Order order) {
        System.out.println("Chef is preparing order with id " + order.getId());
        for (Dish dish : order.getDishes()) {
            if (order.isCancelled()) {
                System.out.println("Order with id " + order.getId() + " has been cancelled. Stopping preparation.");
                return;
            }
            prepareDish(dish);
        }
        order.setStatus(OrderStatus.DONE);
        System.out.println("Chef finished preparing order with id " + order.getId());
    }

    private void prepareDish(Dish dish) {
        System.out.println("Cooking " + dish.getName());
        try {
            Thread.sleep(dish.getTimeToCook());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Finished cooking " + dish.getName());
    }

    // Остановка поваров
    public void shutdown() {
        chefPool.shutdown();
        try {
            if (!chefPool.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                chefPool.shutdownNow();
                if (!chefPool.awaitTermination(60, TimeUnit.MILLISECONDS)) {
                    System.err.println("Chef pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            chefPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
