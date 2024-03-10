package org.example.database;

import com.google.gson.Gson;
import org.example.model.*;

import java.sql.*;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;
import org.example.repository.MoneyStorageImpl;

import java.lang.reflect.Type;
import java.util.List;

public class DataBaseHandler {
    private static volatile Connection connection = null;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root_password";

    private DataBaseHandler() {
    }

    public static Connection getConnectionInstance() {
        if (connection == null) {
            synchronized (DataBaseHandler.class) {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    } catch (SQLException e) {
                        throw new RuntimeException("Failed to connect to the database", e);
                    }
                }
            }
        }
        return connection;
    }

    public static void initialize() throws SQLException {
        Statement statement;
        statement = DataBaseHandler.getConnectionInstance().createStatement();
        statement.execute("CREATE DATABASE IF NOT EXISTS restaurant");
        statement.execute("USE restaurant");
        statement.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, login VARCHAR(100), password VARCHAR(300), isAdmin BOOLEAN)");
        statement.execute("CREATE TABLE IF NOT EXISTS dishes (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), price INT, description VARCHAR(1000), timeToCook BIGINT, quantity INT, rating DOUBLE, ratingCount INT)");
        statement.execute("CREATE TABLE IF NOT EXISTS orders (id INT PRIMARY KEY AUTO_INCREMENT, userId INT, dishes JSON, status VARCHAR(100))");
        statement.execute("CREATE TABLE IF NOT EXISTS moneyStorage (cash INT, noncash INT, total INT)");
        statement.execute("CREATE TABLE IF NOT EXISTS reviews (id INT PRIMARY KEY AUTO_INCREMENT, orderId INT, review VARCHAR(1000))");

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
                        .rating(resultSet.getDouble("rating"))
                        .ratingCount(resultSet.getInt("ratingCount"))
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
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("INSERT INTO dishes (name, price, description, timeToCook, quantity, rating, ratingCount) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, dish.getName());
            preparedStatement.setInt(2, dish.getPrice());
            preparedStatement.setString(3, dish.getDescription());
            preparedStatement.setLong(4, dish.getTimeToCook());
            preparedStatement.setInt(5, dish.getQuantity());
            preparedStatement.setDouble(6, dish.getRating());
            preparedStatement.setInt(7, dish.getRatingCount());

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


    public static void setDishQuantity(int id, int quantity) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE dishes SET quantity = ? WHERE id = ?")) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void increaseDishQuantity(int id) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE dishes SET quantity = quantity + 1 WHERE id = ?")) {
            preparedStatement.setInt(1, id);
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

    public static void addOrder(Order order) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("INSERT INTO orders (userId, dishes, status) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setString(2, new Gson().toJson(order.getDishes()));
            preparedStatement.setString(3, order.getStatus().toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Order> loadOrders() {
        List<Order> orders = new ArrayList<>();
        try (Statement statement = getConnectionInstance().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM orders");
            while (resultSet.next()) {
                String dishesJson = resultSet.getString("dishes");
                Gson gson = new Gson();
                Type dishListType = new TypeToken<List<Dish>>() {
                }.getType();
                List<Dish> dishes = gson.fromJson(dishesJson, dishListType);

                orders.add(Order.builder()
                        .id(resultSet.getInt("id"))
                        .userId(resultSet.getInt("userId"))
                        .dishes(dishes)
                        .status(OrderStatus.valueOf(resultSet.getString("status")))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static void updateOrder(Order activeOrder) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE orders SET dishes = ?, status = ? WHERE id = ?")) {
            preparedStatement.setString(1, new Gson().toJson(activeOrder.getDishes()));
            preparedStatement.setString(2, activeOrder.getStatus().toString());
            preparedStatement.setInt(3, activeOrder.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeOrder(Order order) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("DELETE FROM orders WHERE id = ?")) {
            preparedStatement.setInt(1, order.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Получение данных о наличке из базы
    public static int getCash() {
        try (Statement statement = getConnectionInstance().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM moneyStorage");
            if (resultSet.next()) {
                return resultSet.getInt("cash");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setInitialMoneyStorage();
        return 0;
    }

    // Получение данных о безналичных средствах из базы
    public static int getNonCash() {
        try (Statement statement = getConnectionInstance().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM moneyStorage");
            if (resultSet.next()) {
                return resultSet.getInt("nonCash");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setInitialMoneyStorage();
        return 0;
    }

    private static void setInitialMoneyStorage() {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("INSERT INTO moneyStorage (cash, nonCash, total) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, 0);
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, 0);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Для внесения изменений в кассу в базе
    public static void updateMoneyStorage(MoneyStorageImpl moneyStorage) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE moneyStorage SET cash = ?, nonCash = ?, total = cash + nonCash")) {
            preparedStatement.setInt(1, moneyStorage.getCash());
            preparedStatement.setInt(2, moneyStorage.getNonCash());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Для восстановления данных об отзывах из базы
    public static List<Review> loadReviews() {
        List<Review> reviews = new ArrayList<>();
        try (Statement statement = getConnectionInstance().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM reviews");
            while (resultSet.next()) {
                reviews.add(Review.builder()
                        .id(resultSet.getInt("id"))
                        .review(resultSet.getString("review"))
                        .orderId(resultSet.getInt("orderId"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static void saveReview(Review build) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("INSERT INTO reviews (orderId, review) VALUES (?, ?)")) {
            preparedStatement.setInt(1, build.getOrderId());
            preparedStatement.setString(2, build.getReview());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDishRating(Dish dish) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE dishes SET rating = ?, ratingCount = ? WHERE id = ?")) {
            preparedStatement.setDouble(1, dish.getRating());
            preparedStatement.setInt(2, dish.getRatingCount());
            preparedStatement.setInt(3, dish.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setTimeToCook(int id, long time) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE dishes SET timeToCook = ? WHERE id = ?")) {
            preparedStatement.setLong(1, time);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setPrice(int id, int price) {
        try (PreparedStatement preparedStatement = getConnectionInstance().prepareStatement("UPDATE dishes SET price = ? WHERE id = ?")) {
            preparedStatement.setInt(1, price);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
