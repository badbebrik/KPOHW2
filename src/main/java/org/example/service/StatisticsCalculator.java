package org.example.service;

import org.example.repository.DishesMenuRepositoryImpl;
import org.example.repository.OrderRepositoryImpl;
import org.example.repository.ReviewRepositoryImpl;
import org.example.model.Dish;

import java.util.List;

public class StatisticsCalculator {
    private final OrderRepositoryImpl orderRepo;
    private final ReviewRepositoryImpl reviewRepo;
    private final DishesMenuRepositoryImpl dishesMenu;

    public StatisticsCalculator(OrderRepositoryImpl orderRepo, ReviewRepositoryImpl reviewRepo, DishesMenuRepositoryImpl dishesMenu) {
        this.orderRepo = orderRepo;
        this.reviewRepo = reviewRepo;
        this.dishesMenu = dishesMenu;
    }

    public void showStatistics() {
        System.out.println("Статистика:");
        System.out.println("Количество заказов за весь период: " + orderRepo.getOrderNumber());
        System.out.println("Количество заказов за этот сеанс работы: " + orderRepo.getOrderSessionCounter());
        System.out.println("Количество отзывов: " + reviewRepo.getReviewNumber());

        System.out.println("Статистика по блюдам:");
        System.out.println("Самые популярные блюда: ");
        List<Dish> popularDishes = dishesMenu.getMostPopularDish();
        popularDishes.forEach(dish -> System.out.println(dish.getName() + " - " + dish.getRating()));
        System.out.println("Средняя оценка блюд: " + dishesMenu.getAverageRating());
    }
}
