package org.example.view;

import org.example.DishesMenu;
import org.example.MoneyStorage;
import org.example.StatisticsCalculator;
import org.example.model.Order;

public interface View {

    void showAuthMenu();

    void showRegistrationSuccess(String username);

    void showAdminMenu();

    void showVisitorMenu();

    void showErrorMessage(String message);

    void showMenuItems(DishesMenu dishesMenu);


    void showStatistics(StatisticsCalculator statisticsCalculator);

    void showMoneyStorage(MoneyStorage moneyStorage);

    void showMessageColored(String option, String color);
}
