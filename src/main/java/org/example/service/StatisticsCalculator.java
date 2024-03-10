package org.example.service;

import org.example.model.Dish;

import java.util.List;

public interface StatisticsCalculator {
    public int getTotalOrderNumber();

    public int getTotalOrderSessionCounter();

    public int getTotalReviewNumber();

    public List<Dish> getMostPopularDish();

    public double getAverageRating();
}
