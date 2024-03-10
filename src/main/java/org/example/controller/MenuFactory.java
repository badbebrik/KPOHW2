package org.example.controller;

import org.example.repository.DishesMenuRepositoryImpl;
import org.example.repository.MoneyStorageImpl;
import org.example.repository.OrderRepositoryImpl;
import org.example.repository.ReviewRepositoryImpl;
import org.example.service.Kitchen;
import org.example.model.User;
import org.example.model.UserRole;
import org.example.view.ConsoleView;

public class MenuFactory {
    public static MenuI createMenu(User user, ConsoleView view, DishesMenuRepositoryImpl dishesMenu, Kitchen kitchen, OrderRepositoryImpl orderRepo, MoneyStorageImpl moneyStorage, ReviewRepositoryImpl reviewRepo) {
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