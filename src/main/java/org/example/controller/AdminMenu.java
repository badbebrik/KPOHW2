package org.example.controller;

import lombok.Setter;
import org.example.*;
import org.example.model.Dish;
import org.example.model.User;
import org.example.view.ConsoleView;

import java.util.List;

public class AdminMenu implements MenuI {

    @Setter
    private User currentUser;
    private final DishesMenu dishesMenu;
    private final ConsoleView view;

    private final OrderRepo orderRepo;

    private final ReviewRepo reviewRepo;
    private final MoneyStorage moneyStorage;

    public AdminMenu(User user, ConsoleView view, DishesMenu dishesMenu, Kitchen kitchen, OrderRepo orderRepo, MoneyStorage moneyStorage, ReviewRepo reviewRepo) {
        this.view = view;
        this.dishesMenu = dishesMenu;
        this.currentUser = user;
        this.orderRepo = orderRepo;
        this.moneyStorage = moneyStorage;
        this.reviewRepo = reviewRepo;
    }

    @Override
    public void showMenu() {
        view.showAdminMenu();
    }

    @Override
    public void run() {
        while (true) {
            showMenu();
            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine();
            switch (choice) {
                case 1 -> addDish();
                case 2 -> removeDish();
                case 3 -> showDishes();
                case 4 -> showStatistics();
                case 5 -> setDishQuantity();
                case 6 -> showMoneyStorage();
                case 7 -> showReviews();
                case 8 -> {
                    return;
                }
                default -> view.showErrorMessage("Некорректный ввод. Введите число от 1 до 7");
            }
        }
    }

    private void showMoneyStorage() {
        System.out.println("Касса:");
        System.out.println("Наличные: " + moneyStorage.getCash());
        System.out.println("Безналичные: " + moneyStorage.getNonCash());
        System.out.println("Всего: " + moneyStorage.getTotalMoney());
    }

    private void addDish() {
        System.out.println("Добавление нового блюда в меню:");
        System.out.println("Введите название блюда:");
        String name = Main.scanner.nextLine();
        System.out.println("Введите описание блюда:");
        String description = Main.scanner.nextLine();
        System.out.println("Введите цену блюда:");
        int price = Main.scanner.nextInt();
        System.out.println("Введите количество блюда:");
        int quantity = Main.scanner.nextInt();
        System.out.println("Введите время приготовления блюда:");
        long cookingTime = Main.scanner.nextInt();

        Dish dish = new Dish(name, description, price, quantity, cookingTime);
        dishesMenu.addDish(dish);
    }

    public void showReviews() {
        System.out.println("Отзывы:");
        reviewRepo.getReviews().forEach(review -> System.out.println("id заказа: " + review.getOrderId() + ", Отзыв: " + review.getReview()));
    }

    private void removeDish() {
        System.out.println("Удаление блюда из меню:");
        System.out.println("Введите id блюда:");
        int id = Main.scanner.nextInt();
        dishesMenu.removeDish(id);
    }

    private void showDishes() {
        dishesMenu.forEach(dish -> System.out.println(dish.toString()));
    }

    private void showStatistics() {
        System.out.println("Статистика:");
        System.out.println("Количество заказов за весь период: " + orderRepo.getOrders().size());
        System.out.println("Количество заказов за этот сеанс работы: " + orderRepo.getOrderSessionCounter());
        System.out.println("Количество отзывов: " + reviewRepo.getReviews().size());

        System.out.println("Статистика по блюдам:");
        System.out.println("Самые популярные блюда: ");
        List<Dish> popularDishes = dishesMenu.getMostPopularDish();
        popularDishes.forEach(dish -> System.out.println(dish.getName() + " - " + dish.getRating()));
        System.out.println("Средняя оценка блюд: " + dishesMenu.getAverageRating());
    }

    private void setDishQuantity() {
        System.out.println("Установка количества блюда:");
        System.out.println("Введите id блюда:");
        int id = Main.scanner.nextInt();
        System.out.println("Введите количество:");
        int quantity = Main.scanner.nextInt();
        dishesMenu.setDishQuantity(id, quantity);
    }
}
