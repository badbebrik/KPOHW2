package org.example;

import org.example.controller.AdminMenu;
import org.example.controller.Kitchen;
import org.example.controller.VisitorMenu;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.view.ConsoleView;

public class MenuFactory {

    public static MenuI createMenu(User user, ConsoleView view, DishesMenu dishesMenu, Kitchen kitchen, OrderRepo orderRepo, MoneyStorage moneyStorage, ReviewRepo reviewRepo) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getRole() == UserRole.ADMIN) {
            return new AdminMenu(user, view, dishesMenu, orderRepo, moneyStorage, reviewRepo);
        } else {
            return new VisitorMenu(user, view, dishesMenu, kitchen, orderRepo, moneyStorage, reviewRepo);
        }
    }
}