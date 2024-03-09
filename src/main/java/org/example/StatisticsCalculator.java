package org.example;

import org.example.model.Dish;

import java.util.List;

public class StatisticsCalculator {
    private final OrderRepo orderRepo;
    private final ReviewRepo reviewRepo;
    private final DishesMenu dishesMenu;

    public StatisticsCalculator(OrderRepo orderRepo, ReviewRepo reviewRepo, DishesMenu dishesMenu) {
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
