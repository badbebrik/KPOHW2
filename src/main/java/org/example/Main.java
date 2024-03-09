package org.example;

import org.example.controller.AuthMenu;
import org.example.controller.Kitchen;
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

        DishesMenu dishesMenu = new DishesMenu();
        OrderRepo orderRepo = new OrderRepo();
        Kitchen kitchen = new Kitchen(orderRepo);
        MoneyStorage moneyStorage = new MoneyStorage();
        ReviewRepo reviewRepo = new ReviewRepo();
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