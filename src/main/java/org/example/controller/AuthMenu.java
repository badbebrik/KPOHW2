package org.example.controller;

import org.example.*;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.view.ConsoleView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthMenu {
    static User currentUser = null;
    DishesMenu dishesMenu;
    Kitchen kitchen;

    private final ConsoleView view;

    public AuthMenu(ConsoleView view, DishesMenu dishesMenu, Kitchen kitchen) {
        this.view = view;
        this.dishesMenu = dishesMenu;
        this.kitchen = kitchen;
    }

    // Хэширование пароля методом SHA-512
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

    void showMenu() {
        view.showAuthMenu();
    }

    public void run() {
        while (currentUser == null) {
            showMenu();
            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine();
            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> {
                    return;
                }
            }
        }
    }

    void login() {
        System.out.println("Вход в систему:");
        System.out.println("Введите имя пользователя: ");
        String username = Main.scanner.nextLine();
        System.out.println("Введите пароль: ");
        String password = Main.scanner.nextLine();

        User user = DataBaseHandler.getUser(username);
        if (user != null && user.getPassword().equals(hashPassword(password))) {
            currentUser = user;
            MenuI menu = currentUser.getRole() == UserRole.ADMIN ? new AdminMenu(view, dishesMenu, kitchen) : new VisitorMenu(view, dishesMenu, kitchen);
            menu.setCurrentUser(currentUser);
            menu.run();
        } else {
            System.out.println("Неверное имя пользователя или пароль");
        }

    }


    void register() {
        System.out.println("Регистрация пользователя:");
        System.out.println("Введите служебный ключ для регистрации в качестве администратора: ");
        String adminKey = Main.scanner.nextLine();

        System.out.println("Введите имя пользователя: ");
        String username = Main.scanner.nextLine();
        System.out.println("Введите пароль: ");
        String password = Main.scanner.nextLine();

        currentUser = new User(username, hashPassword(password), adminKey.equals("admin") ? UserRole.ADMIN : UserRole.VISITOR);
        DataBaseHandler.addUser(currentUser);
        MenuI menu = currentUser.getRole() == UserRole.ADMIN ? new AdminMenu(view, dishesMenu, kitchen) : new VisitorMenu(view, dishesMenu, kitchen);
        menu.setCurrentUser(currentUser);
        menu.run();
    }

}
