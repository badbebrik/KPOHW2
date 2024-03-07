package org.example.controller;

import lombok.Setter;
import org.example.DishesMenu;
import org.example.Main;
import org.example.MenuI;
import org.example.OrderRepo;
import org.example.model.Dish;
import org.example.model.User;
import org.example.view.ConsoleView;

public class AdminMenu implements MenuI {

    @Setter
    private User currentUser = null;
    private DishesMenu dishesMenu;
    private final ConsoleView view;

    private OrderRepo orderRepo;

    public AdminMenu(User user, ConsoleView view, DishesMenu dishesMenu, Kitchen kitchen, OrderRepo orderRepo) {
        this.view = view;
        this.dishesMenu = dishesMenu;
        this.currentUser = user;
        this.orderRepo = orderRepo;
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
                case 6 -> {
                    return;
                }
            }
        }
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
