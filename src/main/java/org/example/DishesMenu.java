package org.example;

import org.example.model.Dish;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public void update() {
        dishes = DataBaseHandler.loadDishes();
    }

    public void setDishQuantity(int id, int quantity) {
        DataBaseHandler.setDishQuantity(id, quantity);
        update();
    }

    public void increaseDishQuantity(int id) {
        DataBaseHandler.increaseDishQuantity(id);
        update();
    }

    public Dish getDishById(int id) {
        return dishes.stream().filter(dish -> dish.getId() == id).findFirst().orElse(null);
    }

    public void decreaseDishQuantity(int id) {
        DataBaseHandler.decreaseDishQuantity(id);
        update();
    }

    public void showDishes() {
        dishes.forEach(System.out::println);
    }

    public void updateDishRating(Dish dish) {
        DataBaseHandler.updateDishRating(dish);
        update();
    }

    public List<Dish> getMostPopularDish() {
        List<Dish> mostPopularDishes = new ArrayList<>();
        double maxRating = dishes.stream().mapToDouble(Dish::getRating).max().orElse(0);
        dishes.stream().filter(dish -> dish.getRating() == maxRating).forEach(mostPopularDishes::add);
        return mostPopularDishes;
    }

    public double getAverageRating() {
        double averageRating = dishes.stream().mapToDouble(Dish::getRating).average().orElse(0);
        return Math.round(averageRating * 100.0) / 100.0;
    }

    @Override
    public Iterator<Dish> iterator() {
        return new DishesIterator();
    }

    private class DishesIterator implements Iterator<Dish> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < dishes.size();
        }

        @Override
        public Dish next() {
            return dishes.get(currentIndex++);
        }
    }
}
