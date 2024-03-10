package org.example;

import org.example.controller.AuthMenu;
import org.example.database.DataBaseHandler;
import org.example.repository.DishesMenuRepositoryImpl;
import org.example.repository.MoneyStorageImpl;
import org.example.repository.OrderRepositoryImpl;
import org.example.repository.ReviewRepositoryImpl;
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

        DishesMenuRepositoryImpl dishesMenu = new DishesMenuRepositoryImpl();
        OrderRepositoryImpl orderRepo = new OrderRepositoryImpl();
        Kitchen kitchen = new Kitchen(orderRepo);
        MoneyStorageImpl moneyStorage = new MoneyStorageImpl();
        ReviewRepositoryImpl reviewRepo = new ReviewRepositoryImpl();
        AuthMenu authMenu = new AuthMenu(consoleView, dishesMenu, kitchen, orderRepo, moneyStorage, reviewRepo);
        authMenu.run();
        kitchen.shutdown();
        try {
            DataBaseHandler.getConnectionInstance().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}