package org.example.repository;

import org.example.model.Dish;

public interface DishesMenuRepository extends Iterable<Dish> {
    void addDish(Dish dish);

    void removeDish(int id);

    void update();

    void setDishQuantity(int id, int quantity);

    void setTimeToCook(int id, long time);

    void increaseDishQuantity(int id);

    void decreaseDishQuantity(int id);

    void updateDishRating(Dish dish);

    Dish getDishById(int id);

    void setPrice(int id, int price);

}
