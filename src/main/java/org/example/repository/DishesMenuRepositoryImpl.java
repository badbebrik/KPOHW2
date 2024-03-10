package org.example.repository;

import org.example.database.DataBaseHandler;
import org.example.model.Dish;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DishesMenuRepositoryImpl implements Iterable<Dish>, DishesMenuRepository {
    private List<Dish> dishes;

    public DishesMenuRepositoryImpl() {
        dishes = DataBaseHandler.loadDishes();
    }

    @Override
    public void addDish(Dish dish) {
        DataBaseHandler.saveDish(dish);
        update();
    }

    @Override
    public void removeDish(int id) {
        DataBaseHandler.removeDish(id);
        update();
    }

    @Override
    public void update() {
        dishes = DataBaseHandler.loadDishes();
    }

    @Override
    public void setDishQuantity(int id, int quantity) {
        DataBaseHandler.setDishQuantity(id, quantity);
        update();
    }

    @Override
    public void increaseDishQuantity(int id) {
        DataBaseHandler.increaseDishQuantity(id);
        update();
    }

    @Override
    public Dish getDishById(int id) {
        return dishes.stream().filter(dish -> dish.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void decreaseDishQuantity(int id) {
        DataBaseHandler.decreaseDishQuantity(id);
        update();
    }

    @Override
    public void updateDishRating(Dish dish) {
        DataBaseHandler.updateDishRating(dish);
        update();
    }

    @Override
    public List<Dish> getMostPopularDish() {
        List<Dish> mostPopularDishes = new ArrayList<>();
        double maxRating = dishes.stream().mapToDouble(Dish::getRating).max().orElse(0);
        dishes.stream().filter(dish -> dish.getRating() == maxRating).forEach(mostPopularDishes::add);
        return mostPopularDishes;
    }

    @Override
    public double getAverageRating() {
        double averageRating = dishes.stream().mapToDouble(Dish::getRating).average().orElse(0);
        return Math.round(averageRating * 100.0) / 100.0;
    }

    @Override
    public Iterator<Dish> iterator() {
        return new DishesIterator();
    }

    public void setTimeToCook(int id, long time) {
        DataBaseHandler.setTimeToCook(id, time);
        update();
    }

    @Override
    public void setPrice(int id, int price) {
        DataBaseHandler.setPrice(id, price);
        update();
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
