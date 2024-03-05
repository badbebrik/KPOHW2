package org.example;

import javax.management.StringValueExp;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;

public class AuthMenu {
    static User currentUser = null;

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

    void ShowMenu() {
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");
        System.out.println("3. Выход");
    }

    void run() {
        while (currentUser == null) {
            ShowMenu();
            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine();
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    return;
            }
        }
    }

    void login() {
        System.out.println("Введите логин:");
        String login = Main.scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = Main.scanner.nextLine();
        String hashedPassword = hashPassword(password);
        try {
            DataBase.getInstance().createStatement().execute("USE restaurant");
            var resultSet = DataBase.getInstance().createStatement().executeQuery("SELECT * FROM users WHERE login = '" + login + "' AND password = '" + hashedPassword + "'");
            if (resultSet.next()) {
                System.out.println("Вы успешно вошли");
                if (resultSet.getBoolean("isAdmin")) {
                    currentUser = new Admin(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), true);
                    AdminMenu adminMenu = new AdminMenu();
                    adminMenu.setCurrentAdmin(new Admin(login, password, true));
                    adminMenu.run();
                } else
                    currentUser = new Visitor(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), false);

            } else {
                System.out.println("Неверный логин или пароль");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void register() {
        System.out.println("Введите логин:");
        String login = Main.scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = Main.scanner.nextLine();
        String hashedPassword = hashPassword(password);
        System.out.println("Чтобы зарегистрироваться в качестве администратора, введите служебный код:");
        String code = Main.scanner.nextLine();
        if (code.equals("admin")) {
            try {
                DataBase.getInstance().createStatement().execute("USE restaurant");
                String sql = "INSERT INTO users (login, password, isAdmin) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = DataBase.getInstance().prepareStatement(sql);

                // Установка параметров
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setBoolean(3, true);

                // Выполнение запроса
                preparedStatement.executeUpdate();

                preparedStatement.close();
                System.out.println("Вы успешно зарегистрировались");
                AdminMenu adminMenu = new AdminMenu();
                adminMenu.setCurrentAdmin(new Admin(login, password, true));
                adminMenu.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            DataBase.getInstance().createStatement().execute("USE restaurant");
            DataBase.getInstance().createStatement().execute("INSERT INTO users (login, password, isAdmin) VALUES ('" + login + "', '" + hashedPassword + "', false)");
            System.out.println("Вы успешно зарегистрировались");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
