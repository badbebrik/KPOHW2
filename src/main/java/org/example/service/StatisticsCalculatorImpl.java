package org.example.service;

import org.example.repository.*;
import org.example.model.Dish;

import java.util.ArrayList;
import java.util.List;

public class StatisticsCalculatorImpl implements StatisticsCalculator {
    private final OrderRepository orderRepo;
    private final ReviewRepository reviewRepo;
    private final DishesMenuRepository dishesMenu;

    public StatisticsCalculatorImpl(OrderRepository orderRepo, ReviewRepository reviewRepo, DishesMenuRepository dishesMenu) {
        this.orderRepo = orderRepo;
        this.reviewRepo = reviewRepo;
        this.dishesMenu = dishesMenu;
    }

    @Override
    public int getTotalOrderNumber() {
        return orderRepo.getOrderNumber();
    }

    @Override
    public int getTotalOrderSessionCounter() {
        return orderRepo.getOrderSessionCounter();
    }

    @Override
    public int getTotalReviewNumber() {
        return reviewRepo.getReviewNumber();
    }

    @Override
    public List<Dish> getMostPopularDish() {
        List<Dish> mostPopularDishes = new ArrayList<>();
        double maxRating = 0;
        for (Dish dish : dishesMenu) {
            if (dish.getRating() > maxRating) {
                mostPopularDishes.clear();
                mostPopularDishes.add(dish);
                maxRating = dish.getRating();
            } else if (dish.getRating() == maxRating) {
                mostPopularDishes.add(dish);
            }
        }

        return mostPopularDishes;
    }

    @Override
    public double getAverageRating() {
        double sum = 0;
        int count = 0;
        for (Dish dish : dishesMenu) {
            if (dish.getRating() == 0) {
                continue;
            }
            sum += dish.getRating();
            count++;
        }

        if (count == 0) {
            return 0;
        }

        return sum / count;
    }
}
