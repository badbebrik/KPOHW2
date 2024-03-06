package org.example.view;

import org.example.DishesMenu;
import org.example.model.Order;

public interface View {

    void showAuthMenu();

    void showRegistrationSuccess(String username);

    void showAdminMenu();

    void showVisitorMenu();

    void showErrorMessage(String message);

    void showMenuItems(DishesMenu dishesMenu);

    void showOrderItems(Order order);

    void showOrderSuccess();

    void showOrderCancel();

    void showOrderDone(String id);

    void showOrderPaid(String id);

    void showPaymentMenu();

    void showStatistics();

}
