package org.example;

import org.example.controller.AuthMenu;
import org.example.view.ConsoleView;

import java.awt.*;
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
        ConsoleView consoleView = new ConsoleView();
        DishesMenu dishesMenu = new DishesMenu();
        AuthMenu authMenu = new AuthMenu(consoleView, dishesMenu);
        authMenu.run();
    }
}