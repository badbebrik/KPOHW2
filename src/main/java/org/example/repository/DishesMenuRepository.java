package org.example.repository;

import org.example.model.Dish;

import java.util.List;

public interface DishesMenuRepository {
    void addDish(Dish dish);

    void removeDish(int id);

    void update();

    void setDishQuantity(int id, int quantity);

    void setTimeToCook(int id, long time);

    void increaseDishQuantity(int id);

    void decreaseDishQuantity(int id);

    void updateDishRating(Dish dish);

    Dish getDishById(int id);

    List<Dish> getMostPopularDish();

    double getAverageRating();

    void setPrice(int id, int price);

}
