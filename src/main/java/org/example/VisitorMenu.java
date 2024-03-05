package org.example;

import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;

public class VisitorMenu {

    @Setter
    @Getter
    private Visitor currentVisitor = null;


    private void showMenu() {
        System.out.println("1. Make Order");
        System.out.println("2. Show Menu");
        System.out.println("3. Add Dish to active order");
        System.out.println("4. Show active orders status");
        System.out.println("5. Cancel order");
        System.out.println("6. Pay for order");
        System.out.println("7. Exit");
    }

    public void run() {
        while (true) {
            showMenu();
            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine();
            switch (choice) {
                case 1:
                    makeOrder();
                    break;
                case 2:
                    showDishes();
                    break;
                case 3:
//                    addDishToActiveOrder();
                    break;
                case 4:
//                    showActiveOrdersStatus();
                    break;
                case 5:
//                    cancelOrder();
                    break;
                case 6:
//                    payForOrder();
                    break;
                case 7:
                    return;
            }
        }
    }

    private void makeOrder() {

    }

    private void showDishes() {
        System.out.println("Меню ресторана: ");
        try {
            DataBase.getInstance().createStatement().execute("USE restaurant");
            var resultSet = DataBase.getInstance().createStatement().executeQuery("SELECT * FROM dishes");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " " + resultSet.getString("name") + " " + resultSet.getInt("price") + " " + resultSet.getString("description") + " " + resultSet.getLong("timeToCook"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
