package org.example.controller;

import org.example.*;
import org.example.database.DataBaseHandler;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.repository.*;
import org.example.service.Kitchen;
import org.example.view.ConsoleColors;
import org.example.view.ConsoleView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthMenu {
    static User currentUser = null;
    DishesMenuRepository dishesMenu;
    Kitchen kitchen;
    OrderRepository orderRepo;
    MoneyStorage moneyStorage;
    ReviewRepository reviewRepo;
    private final ConsoleView view;

    public AuthMenu(ConsoleView view, DishesMenuRepository dishesMenu, Kitchen kitchen, OrderRepository orderRepo, MoneyStorage moneyStorage, ReviewRepository reviewRepo) {
        this.view = view;
        this.dishesMenu = dishesMenu;
        this.kitchen = kitchen;
        this.orderRepo = orderRepo;
        this.moneyStorage = moneyStorage;
        this.reviewRepo = reviewRepo;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showMenu() {
        view.showAuthMenu();
    }

    public void run() {
        while (true) {
            showMenu();
            int choice;
            try {
                choice = Main.scanner.nextInt();
            }
            catch (Exception e) {
                view.showErrorMessage("Некорректный ввод. Введите число от 1 до 3");
                Main.scanner.nextLine();
                continue;
            }

            Main.scanner.nextLine();
            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> {
                    return;
                }
                default -> view.showErrorMessage("Некорректный ввод. Введите число от 1 до 3");
            }
        }
    }

    private void login() {
        view.showMessageColored("Вход в систему", ConsoleColors.ANSI_BLUE);
        System.out.println("Введите имя пользователя: ");
        String username = Main.scanner.nextLine();
        System.out.println("Введите пароль: ");
        String password = Main.scanner.nextLine();

        User user = DataBaseHandler.getUser(username);
        if (user != null && user.getPassword().equals(hashPassword(password))) {
            currentUser = user;
            MenuI menu = MenuFactory.createMenu(currentUser, view, dishesMenu, kitchen, orderRepo, moneyStorage, reviewRepo);
            menu.run();
        } else {
            view.showErrorMessage("Неверное имя пользователя или пароль");
        }
    }

    private void register() {
        view.showMessageColored("Регистрация", ConsoleColors.ANSI_BLUE);
        System.out.println("Введите служебный ключ для регистрации в качестве администратора: ");
        String adminKey = Main.scanner.nextLine();

        System.out.println("Введите имя пользователя: ");
        String username = Main.scanner.nextLine();
        if (DataBaseHandler.getUser(username) != null) {
            view.showErrorMessage("Пользователь с таким именем уже существует");
            return;
        }

        System.out.println("Введите пароль: ");
        String password = Main.scanner.nextLine();

        currentUser = new User(username, hashPassword(password), adminKey.equals("admin") ? UserRole.ADMIN : UserRole.VISITOR);
        view.showRegistrationSuccess(username);
        DataBaseHandler.addUser(currentUser);
        currentUser = DataBaseHandler.getUser(username);
        MenuI menu = MenuFactory.createMenu(currentUser, view, dishesMenu, kitchen, orderRepo, moneyStorage, reviewRepo);
        menu.run();
    }
}
