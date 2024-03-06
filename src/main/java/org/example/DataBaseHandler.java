package org.example;

import org.example.model.Dish;
import org.example.model.User;
import org.example.model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Singleton
public class DataBaseHandler {
    private static volatile Connection connection = null; // Добавляем volatile для корректной работы double-checked locking

    private DataBaseHandler() {
    } // Приватный конструктор, чтобы предотвратить создание экземпляров извне

    public static Connection getConnectionInstance() {
        if (connection == null) { // Первая проверка на наличие экземпляра для уменьшения накладных расходов
            synchronized (DataBaseHandler.class) {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "24244650Vas");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return connection;
    }

    public static List<Dish> loadDishes() {
        List<Dish> dishes = new ArrayList<>();
        try (Statement statement = getConnectionInstance().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM dishes");
            while (resultSet.next()) {
                dishes.add(Dish.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .price(resultSet.getInt("price"))
                        .description(resultSet.getString("description"))
                        .timeToCook(resultSet.getLong("timeToCook"))
                        .quantity(resultSet.getInt("quantity"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    public static void closeConnection() {
        try {
            getConnectionInstance().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveDish(Dish dish) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("INSERT INTO dishes (name, price, description, timeToCook, quantity) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, dish.getName());
            preparedStatement.setInt(2, dish.getPrice());
            preparedStatement.setString(3, dish.getDescription());
            preparedStatement.setLong(4, dish.getTimeToCook());
            preparedStatement.setInt(5, dish.getQuantity());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeDish(int id) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("DELETE FROM dishes WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User user) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("INSERT INTO users (login, password, isAdmin) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBoolean(3, user.getRole() == UserRole.ADMIN);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUser(String login) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("SELECT * FROM users WHERE login = ?")) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return User.builder()
                        .id(resultSet.getInt("id"))
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .role(resultSet.getBoolean("isAdmin") ? UserRole.ADMIN : UserRole.VISITOR)
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void increaseDishQuantity(int id, int quantity) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE dishes SET quantity = quantity + ? WHERE id = ?")) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void decreaseDishQuantity(int id) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE dishes SET quantity = quantity - 1 WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
