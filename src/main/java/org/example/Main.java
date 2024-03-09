package org.example;

import org.example.controller.AuthMenu;
import org.example.controller.Kitchen;
import org.example.view.ConsoleView;
import java.sql.*;
import java.util.Scanner;


public class Main {
    public static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) throws SQLException {
        Statement statement = DataBaseHandler.getConnectionInstance().createStatement();
        statement.execute("CREATE DATABASE IF NOT EXISTS restaurant");
        statement.execute("USE restaurant");
        statement.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, login VARCHAR(100), password VARCHAR(300), isAdmin BOOLEAN)");
        statement.execute("CREATE TABLE IF NOT EXISTS dishes (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), price INT, description VARCHAR(1000), timeToCook BIGINT, quantity INT)");
        statement.execute("CREATE TABLE IF NOT EXISTS orders (id INT PRIMARY KEY AUTO_INCREMENT, userId INT, dishes JSON, status VARCHAR(100))");
        statement.execute("CREATE TABLE IF NOT EXISTS moneyStorage (cash INT, noncash INT, total INT)");
        statement.execute("CREATE TABLE IF NOT EXISTS reviews (id INT PRIMARY KEY AUTO_INCREMENT, orderId INT, review VARCHAR(1000))");
        ConsoleView consoleView = new ConsoleView();
        DishesMenu dishesMenu = new DishesMenu();
        OrderRepo orderRepo = new OrderRepo();
        Kitchen kitchen = new Kitchen(3, orderRepo);
        MoneyStorage moneyStorage = new MoneyStorage();
        ReviewRepo reviewRepo = new ReviewRepo();
        AuthMenu authMenu = new AuthMenu(consoleView, dishesMenu, kitchen, orderRepo, moneyStorage, reviewRepo);
        authMenu.run();
        kitchen.shutdown();
    }
}