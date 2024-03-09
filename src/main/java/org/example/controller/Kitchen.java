package org.example.controller;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.example.OrderRepo;
import org.example.model.Dish;
import org.example.model.Order;
import org.example.model.OrderStatus;


import java.util.concurrent.*;

public class Kitchen {
    private final ThreadPoolExecutor chefPool;
    private final LinkedBlockingQueue<Order> orderQueue;

    private final OrderRepo orderRepo;

    public Kitchen(int numberOfChefs, OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
        orderQueue = new LinkedBlockingQueue<>();
        chefPool = new ThreadPoolExecutor(numberOfChefs, numberOfChefs, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        fillOrderQueue();
        startProcessingOrders();
    }

    public void addOrder(Order order, Runnable callback) {
        orderQueue.add(order);
    }

    public void fillOrderQueue() {
        for (Order order : orderRepo.getOrders()) {
            if (order.getStatus() == OrderStatus.IN_PROGRESS) {
                orderQueue.add(order);
            }
        }
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
        for (Dish dish : order.getDishes()) {
            if (order.isCancelled()) {
                System.out.println("Order with id " + order.getId() + " has been cancelled. Stopping preparation.");
                return;
            }
            prepareDish(dish);
        }
        order.setStatus(OrderStatus.DONE);
        System.out.println("Заказ для пользователя " + order.getUserId() + " готов");
    }

    private void prepareDish(Dish dish) {
        try {
            Thread.sleep(dish.getTimeToCook());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
