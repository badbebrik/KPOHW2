package org.example;

import org.example.model.Dish;
import org.example.model.Order;
import org.example.model.OrderStatus;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DishesMenu implements Iterable<Dish> {
    private List<Dish> dishes;

    public DishesMenu() {
        dishes = DataBaseHandler.loadDishes();
    }

    public void addDish(Dish dish) {
        DataBaseHandler.saveDish(dish);
        update();
    }

    public void removeDish(int id) {
        DataBaseHandler.removeDish(id);
        update();
    }


    @Override
    public Iterator<Dish> iterator() {
        return dishes.iterator();
    }

    @Override
    public void forEach(Consumer<? super Dish> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Dish> spliterator() {
        return Iterable.super.spliterator();
    }

    private void update() {
        dishes = DataBaseHandler.loadDishes();
    }

    public void increaseDishQuantity(int id, int quantity) {
        DataBaseHandler.increaseDishQuantity(id, quantity);
        update();
    }

    public Dish getDishById(int id) {
        return dishes.stream().filter(dish -> dish.getId() == id).findFirst().orElse(null);
    }

    public void decreaseDishQuantity(int id) {
        DataBaseHandler.decreaseDishQuantity(id);
        update();
    }

    public void addOrder(Order activeOrder) {
        DataBaseHandler.addOrder(activeOrder);
    }

    public void updateOrder(Order activeOrder) {
        DataBaseHandler.updateOrder(activeOrder);
    }

    public Order getActiveOrderByUserId(int id) {
        return DataBaseHandler.loadOrders().stream().filter(order -> order.getUserId() == id)
                .filter(order -> order.getStatus() != OrderStatus.PAID).findFirst().orElse(null);
    }

    public void showDishes() {
        dishes.forEach(System.out::println);
    }
}
