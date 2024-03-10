package org.example.controller;

import lombok.Setter;
import org.example.*;
import org.example.model.Dish;
import org.example.model.User;
import org.example.repository.DishesMenuRepositoryImpl;
import org.example.repository.MoneyStorageImpl;
import org.example.repository.OrderRepositoryImpl;
import org.example.repository.ReviewRepositoryImpl;
import org.example.service.StatisticsCalculator;
import org.example.view.ConsoleColors;
import org.example.view.ConsoleView;

public class AdminMenu implements MenuI {

    @Setter
    private User currentUser;
    private final DishesMenuRepositoryImpl dishesMenu;
    private final ConsoleView view;
    private final OrderRepositoryImpl orderRepo;
    private final ReviewRepositoryImpl reviewRepo;
    private final MoneyStorageImpl moneyStorage;

    public AdminMenu(User user, ConsoleView view, DishesMenuRepositoryImpl dishesMenu, OrderRepositoryImpl orderRepo, MoneyStorageImpl moneyStorage, ReviewRepositoryImpl reviewRepo) {
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
                case 6 -> setTimeToCook();
                case 7 -> setPrice();
                case 8 -> showMoneyStorage();
                case 9 -> showReviews();
                case 10 -> {
                    return;
                }
                default -> view.showErrorMessage("Некорректный ввод. Введите число от 1 до 10");
            }
        }
    }

    private void showMoneyStorage() {
        view.showMoneyStorage(moneyStorage);
    }

    private void addDish() {
        String name;
        String description;
        int price;
        int quantity;
        long cookingTime;

        view.showMessageColored("Добавление блюда в меню:", ConsoleColors.ANSI_BLUE);
        try {
            System.out.println("Введите название блюда:");
            name = Main.scanner.nextLine();
            System.out.println("Введите описание блюда:");
            description = Main.scanner.nextLine();
            System.out.println("Введите цену блюда:");
            price = Main.scanner.nextInt();
            System.out.println("Введите количество блюда:");
            quantity = Main.scanner.nextInt();
            System.out.println("Введите время приготовления блюда (в секундах):");
            cookingTime = Main.scanner.nextInt();
        } catch (Exception e) {
            view.showErrorMessage("Некорректный ввод");
            return;
        }

        Dish dish = Dish.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .timeToCook(cookingTime)
                .build();

        dishesMenu.addDish(dish);
        view.showMessageColored("Блюдо добавлено", ConsoleColors.ANSI_GREEN);
    }

    public void showReviews() {
        view.showMessageColored("Отзывы:", ConsoleColors.ANSI_BLUE);
        reviewRepo.forEach(review -> System.out.println("id заказа: " + review.getOrderId() + ", Отзыв: " + review.getReview()));
    }

    private void removeDish() {
        view.showMessageColored("Удаление блюда из меню:", ConsoleColors.ANSI_BLUE);
        System.out.println("Введите id блюда:");
        int id;
        try {
            id = Main.scanner.nextInt();
        } catch (Exception e) {
            view.showErrorMessage("Некорректный ввод");
            return;
        }

        dishesMenu.removeDish(id);
    }

    private void showDishes() {
        view.showMenuItemsAdmin(dishesMenu);
    }

    private void showStatistics() {
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator(orderRepo, reviewRepo, dishesMenu);
        view.showStatistics(statisticsCalculator);
    }

    private void setDishQuantity() {
        view.showMessageColored("Изменение количества блюда:", ConsoleColors.ANSI_BLUE);
        System.out.println("Введите id блюда:");
        int id, quantity;
        try {
            id = Main.scanner.nextInt();
            System.out.println("Введите количество:");
            quantity = Main.scanner.nextInt();
        } catch (Exception e) {
            view.showErrorMessage("Некорректный ввод");
            return;
        }

        dishesMenu.setDishQuantity(id, quantity);
    }

    private void setTimeToCook() {
        if (orderRepo.anyOrderIsInProgressOrNew()) {
            view.showErrorMessage("Нельзя изменить время приготовления блюд, пока есть активные созданные или готовящиеся заказы");
            return;
        }

        view.showMessageColored("Изменение времени приготовления блюда:", ConsoleColors.ANSI_BLUE);
        System.out.println("Введите id блюда:");
        int id;
        long time;
        try {
            id = Main.scanner.nextInt();
            System.out.println("Введите время приготовления (в секундах):");
            time = Main.scanner.nextLong();
        } catch (Exception e) {
            view.showErrorMessage("Некорректный ввод");
            return;
        }

        dishesMenu.setTimeToCook(id, time);
    }

    private void setPrice() {

        if (orderRepo.anyOrderIsInProgressOrNew()) {
            view.showErrorMessage("Нельзя изменить цену блюд, пока есть активные созданные или готовящиеся заказы");
            return;
        }

        view.showMessageColored("Изменение цены блюда:", ConsoleColors.ANSI_BLUE);
        System.out.println("Введите id блюда:");
        int id, price;
        try {
            id = Main.scanner.nextInt();
            System.out.println("Введите цену:");
            price = Main.scanner.nextInt();
        } catch (Exception e) {
            view.showErrorMessage("Некорректный ввод");
            return;
        }
        dishesMenu.setPrice(id, price);
    }

}
