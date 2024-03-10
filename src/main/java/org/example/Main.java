package org.example;

import org.example.controller.AuthMenu;
import org.example.database.DataBaseHandler;
import org.example.repository.*;
import org.example.service.Kitchen;
import org.example.view.ConsoleView;

import java.sql.*;
import java.util.Scanner;


public class Main {
    public static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        ConsoleView consoleView = new ConsoleView();
        try {
            DataBaseHandler.initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DishesMenuRepository dishesMenu = new DishesMenuRepositoryImpl();
        OrderRepository orderRepo = new OrderRepositoryImpl();
        Kitchen kitchen = new Kitchen(orderRepo);
        MoneyStorage moneyStorage = new MoneyStorageImpl();
        ReviewRepository reviewRepo = new ReviewRepositoryImpl();
        AuthMenu authMenu = new AuthMenu(consoleView, dishesMenu, kitchen, orderRepo, moneyStorage, reviewRepo);
        authMenu.run();
        kitchen.shutdown();
        DataBaseHandler.closeConnection();
    }
}