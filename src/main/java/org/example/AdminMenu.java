package org.example;

import lombok.Setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminMenu {

    @Setter
    private  Admin currentAdmin = null;
    private void showMenu() {
        System.out.println("1. Добавить блюдо");
        System.out.println("2. Удалить блюдо");
        System.out.println("3. Редактировать блюдо");
        System.out.println("4. Показать меню");
        System.out.println("5. Выход");
    }

    public void run() {
        while (true) {
            showMenu();
            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine();
            switch (choice) {
                case 1:
                    addDish();
                    break;
                case 2:
                    removeDish();
                    break;
                case 3:
                    updateDish();
                    break;
                case 4:
                    showDishes();
                    break;
                case 5:
                    return;
            }
        }
    }

    private void addDish() {
        System.out.println("Введите название блюда:");
        String name = Main.scanner.nextLine();
        System.out.println("Введите цену:");
        int price = Main.scanner.nextInt();
        Main.scanner.nextLine();
        System.out.println("Введите описание:");
        String description = Main.scanner.nextLine();
        System.out.println("Введите время приготовления:");
        long timeToCook = Main.scanner.nextLong();
        Main.scanner.nextLine();

        try {
            DataBase.getInstance().createStatement().execute("USE restaurant");
            PreparedStatement ps = DataBase.getInstance().prepareStatement("INSERT INTO dishes (name, price, description, timeToCook) VALUES (?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setString(3, description);
            ps.setLong(4, timeToCook);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeDish() {
        System.out.println("Введите id блюда:");
        int id = Main.scanner.nextInt();
        Main.scanner.nextLine();
        try {
            DataBase.getInstance().createStatement().execute("USE restaurant");
            DataBase.getInstance().createStatement().execute("DELETE FROM dishes WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    private void updateDish() {
        System.out.println("Введите id блюда:");
        int id = Main.scanner.nextInt();
        Main.scanner.nextLine();
        System.out.println("Введите новое название блюда:");
        String name = Main.scanner.nextLine();
        System.out.println("Введите новую цену:");
        int price = Main.scanner.nextInt();
        Main.scanner.nextLine();
        System.out.println("Введите новое описание:");
        String description = Main.scanner.nextLine();
        System.out.println("Введите новое время приготовления:");
        long timeToCook = Main.scanner.nextLong();
        Main.scanner.nextLine();
        try {
            DataBase.getInstance().createStatement().execute("USE restaurant");
            PreparedStatement ps = DataBase.getInstance().prepareStatement("UPDATE dishes SET name = ?, price = ?, description = ?, timeToCook = ? WHERE id = ?");
            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setString(3, description);
            ps.setLong(4, timeToCook);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showDishes() {
        try {
            DataBase.getInstance().createStatement().execute("USE restaurant");
            var resultSet = DataBase.getInstance().createStatement().executeQuery("SELECT * FROM dishes");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " " + resultSet.getString("name") + " " + resultSet.getInt("price") + " " + resultSet.getString("description") + " " + resultSet.getLong("timeToCook"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
