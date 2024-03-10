package org.example.view;

import org.example.repository.DishesMenuRepositoryImpl;
import org.example.repository.MoneyStorageImpl;
import org.example.service.StatisticsCalculator;

public interface View {

    void showAuthMenu();

    void showRegistrationSuccess(String username);

    void showAdminMenu();

    void showVisitorMenu();

    void showErrorMessage(String message);

    void showMenuItemsAdmin(DishesMenuRepositoryImpl dishesMenu);

    void showMenuItemsVisitor(DishesMenuRepositoryImpl dishesMenu);


    void showStatistics(StatisticsCalculator statisticsCalculator);

    void showMoneyStorage(MoneyStorageImpl moneyStorage);

    void showMessageColored(String option, String color);
}
