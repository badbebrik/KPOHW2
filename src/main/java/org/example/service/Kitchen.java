package org.example.service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.example.repository.OrderRepository;
import org.example.model.Dish;
import org.example.model.Order;
import org.example.model.OrderStatus;


import java.util.concurrent.*;

public class Kitchen {

    private static final int NUMBER_OF_CHEFS = 3;
    private final ThreadPoolExecutor chefPool;
    private final LinkedBlockingQueue<Order> orderQueue;

    private final OrderRepository orderRepo;
    private final int MILLIS_PER_SECOND = 1000;
    private final int TIMEOUT = 60;

    public Kitchen(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
        orderQueue = new LinkedBlockingQueue<>();
        chefPool = new ThreadPoolExecutor(NUMBER_OF_CHEFS, NUMBER_OF_CHEFS, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        fillOrderQueue();
        startProcessingOrders();
    }

    public void addOrder(Order order) {
        orderQueue.add(order);
    }

    public void cancelOrder(Order order) {
        order.setCancelled(true);
        removeOrder(order);
    }

    private void removeOrder(Order order) {
        orderQueue.remove(order);
    }

    private void fillOrderQueue() {
        for (Order order : orderRepo) {
            if (order.getStatus() == OrderStatus.IN_PROGRESS) {
                orderQueue.add(order);
            }
        }
    }

    private void startProcessingOrders() {
        Runnable task = () -> {
            while (true) {
                try {
                    Order order = orderQueue.take();
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
            prepareDish(dish);
        }

        if (Thread.currentThread().isInterrupted()) {
            order.setStatus(OrderStatus.IN_PROGRESS);
        } else {
            order.setStatus(OrderStatus.DONE);
        }

        orderRepo.updateOrder(order);
        order = orderRepo.getActiveOrderByUserId(order.getUserId());

        if (order == null) {
            return;
        }

        if (order.getStatus() == OrderStatus.DONE) {
            System.out.println("Заказ для пользователя " + order.getUserId() + " готов");
        }
    }

    private void prepareDish(Dish dish) {
        try {
            Thread.sleep(dish.getTimeToCook() * MILLIS_PER_SECOND);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Остановка поваров
    public void shutdown() {
        chefPool.shutdown();
        try {
            if (!chefPool.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS)) {
                chefPool.shutdownNow();
                if (!chefPool.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS)) {
                    System.err.println("Повара не остановились");
                }
            }
        } catch (InterruptedException e) {
            chefPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
