package org.example;

import java.sql.*;
import java.util.Scanner;


public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static Menu menu = new Menu();


    public static void main(String[] args) throws SQLException {
        Statement statement = DataBase.getInstance().createStatement();
        statement.execute("CREATE DATABASE IF NOT EXISTS restaurant");
        statement.execute("USE restaurant");
        statement.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, login VARCHAR(100), password VARCHAR(300), isAdmin BOOLEAN)");
        statement.execute("CREATE TABLE IF NOT EXISTS dishes (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), price INT, description VARCHAR(1000), timeToCook BIGINT)");
        statement.execute("CREATE TABLE IF NOT EXISTS orders (id INT PRIMARY KEY AUTO_INCREMENT, userId INT, dishId INT, FOREIGN KEY (userId) REFERENCES users(id), FOREIGN KEY (dishId) REFERENCES dishes(id))");


        AuthMenu authMenu = new AuthMenu();
        authMenu.run();
    }
}