package org.example.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.example.model.Dish;
import org.example.model.Order;
import org.example.model.OrderStatus;

public class Kitchen {
    private final ExecutorService chefPool;
    private final LinkedBlockingQueue<Order> orderQueue;

    public Kitchen(int numberOfChefs) {
        chefPool = Executors.newFixedThreadPool(numberOfChefs);
        orderQueue = new LinkedBlockingQueue<>();
        startProcessingOrders();
    }

    public void addOrder(Order order) {
        try {
            orderQueue.put(order);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void startProcessingOrders() {
        Runnable task = () -> {
            while (true) {
                try {
                    Order order = orderQueue.take(); // Блокирующая операция - ждем заказа
                    processOrder(order);
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
            prepareDish(dish);
        }
        order.setStatus(OrderStatus.DONE);
        System.out.println("Chef finished preparing order with id " + order.getId());
    }

    private void prepareDish(Dish dish) {
        System.out.println("Cooking " + dish.getName());
        // Simulating cooking time
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
            if (!chefPool.awaitTermination(60, TimeUnit.SECONDS)) {
                chefPool.shutdownNow();
                if (!chefPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Chef pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            chefPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

