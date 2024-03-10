package org.example.controller;

import lombok.Setter;
import org.example.*;
import org.example.model.Dish;
import org.example.model.User;
import org.example.view.ConsoleView;

public class AdminMenu implements MenuI {

    @Setter
    private User currentUser;
    private final DishesMenu dishesMenu;
    private final ConsoleView view;
    private final OrderRepo orderRepo;
    private final ReviewRepo reviewRepo;
    private final MoneyStorage moneyStorage;

    public AdminMenu(User user, ConsoleView view, DishesMenu dishesMenu, OrderRepo orderRepo, MoneyStorage moneyStorage, ReviewRepo reviewRepo) {
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
                default -> view.showErrorMessage("Некорректный ввод. Введите число от 1 до 8");
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
        view.showMenuItems(dishesMenu);
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

}
