package org.example.view;

import org.example.repository.DishesMenuRepository;
import org.example.repository.MoneyStorage;
import org.example.service.StatisticsCalculator;


public interface View {

    void showAuthMenu();

    void showRegistrationSuccess(String username);

    void showAdminMenu();

    void showVisitorMenu();

    void showErrorMessage(String message);

    void showMenuItemsAdmin(DishesMenuRepository dishesMenu);

    void showMenuItemsVisitor(DishesMenuRepository dishesMenu);


    void showStatistics(StatisticsCalculator statisticsCalculator);

    void showMoneyStorage(MoneyStorage moneyStorage);

    void showMessageColored(String option, String color);
}
