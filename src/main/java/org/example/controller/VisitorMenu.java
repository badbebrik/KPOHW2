package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.DishesMenu;
import org.example.Main;
import org.example.MenuI;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.User;
import org.example.view.ConsoleView;

public class VisitorMenu implements MenuI {

    @Setter
    @Getter
    private User currentUser = null;
    private DishesMenu dishesMenu;

    private Order activeOrder;
    private final ConsoleView view;

    public VisitorMenu(ConsoleView view, DishesMenu dishesMenu) {
        this.view = view;
        this.dishesMenu = dishesMenu;
    }


    public void showMenu() {
        view.showVisitorMenu();
    }

    public void run() {
        while (true) {
            showMenu();
            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine();
            switch (choice) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    showOrder();
                    break;
                case 3:
                    addDishToActiveOrder();
                    break;
                case 4:
                    showActiveOrdersStatus();
                    break;
                case 5:
                    cancelOrder();
                    break;
                case 6:
                    payForOrder();
                    break;
                case 7:
                    return;
            }
        }
    }

    private void createOrder() {
        System.out.println("Вы создали новый заказ. Добавьте блюда в заказ и оформите его.");
        activeOrder = new Order();
        activeOrder.setStatus(OrderStatus.NEW);
    }

    private void showOrder() {
        dishesMenu.forEach(dish -> System.out.println(dish.getName() + " " + dish.getPrice()));
        for (int i = 0; i < activeOrder.getDishes().size(); i++) {
            System.out.println(activeOrder.getDishes().get(i).getName() + " " + activeOrder.getDishes().get(i).getPrice());
        }
    }

    private void addDishToActiveOrder() {
        showOrder();
        if (activeOrder.getStatus() == OrderStatus.DONE) {
            System.out.println("Невозможно добавить блюдо в выполненный заказ.");
            return;
        }
        System.out.println("Добавление блюда в заказ:");
        System.out.println("Введите id блюда:");
        int id = Main.scanner.nextInt();
        Main.scanner.nextLine();
        if (dishesMenu.getDishById(id) != null && dishesMenu.getDishById(id).getQuantity() > 0) {
            activeOrder.addDish(dishesMenu.getDishById(id));
            dishesMenu.decreaseDishQuantity(id);
        } else {
            System.out.println("Блюда с таким id не существует");
        }

    }

    private void showActiveOrdersStatus() {
        System.out.println("Статус заказа: " + activeOrder.getStatus());
    }

    private void cancelOrder() {

    }

    private void payForOrder() {

    }
}
