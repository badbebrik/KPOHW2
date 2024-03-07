package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.DishesMenu;
import org.example.Main;
import org.example.MenuI;
import org.example.model.Dish;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.User;
import org.example.view.ConsoleView;

import java.util.Iterator;

public class VisitorMenu implements MenuI {

    @Setter
    @Getter
    private User currentUser = null;
    private DishesMenu dishesMenu;
    private Kitchen kitchen;

    private Order activeOrder;
    private final ConsoleView view;

    public VisitorMenu(User user, ConsoleView view, DishesMenu dishesMenu, Kitchen kitchen) {
        this.view = view;
        this.dishesMenu = dishesMenu;
        this.kitchen = kitchen;
        this.currentUser = user;
        activeOrder = dishesMenu.getActiveOrderByUserId(currentUser.getId());
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
                    makeOrder();
                    break;
                case 8:
                    AuthMenu authMenu = new AuthMenu(view, dishesMenu, kitchen);
                    authMenu.run();
                    return;
            }
        }
    }

    private void createOrder() {
        if (activeOrder != null && activeOrder.getStatus() != OrderStatus.PAID) {

            if (activeOrder.getStatus() == OrderStatus.NEW) {
                System.out.println("У вас уже есть новый заказ. Добавьте блюда в заказ и оформите его.");
                return;
            }

            if (activeOrder.getStatus() == OrderStatus.DONE) {
                System.out.println("Вы не оплатили прошлый заказ. Оплатите и создайте новый.");
                return;
            }

            if (activeOrder.getStatus() == OrderStatus.IN_PROGRESS) {
                System.out.println("Ваш заказ уже готовится. Дождитесь его выполнения.");
                return;
            }
        }

        System.out.println("Вы создали новый заказ. Добавьте блюда в заказ и оформите его.");
        activeOrder = new Order();
        activeOrder.setStatus(OrderStatus.NEW);
        activeOrder.setUserId(currentUser.getId());
        dishesMenu.addOrder(activeOrder);
        loadOrder();
    }

    private void showDishes() {
        dishesMenu.showDishes();
    }

    private void showOrder() {
        Iterator<Dish> iterator = activeOrder.iterator();
        if (!iterator.hasNext()) {
            System.out.println("Ваш заказ пуст");
            return;
        }
        System.out.println("Ваш заказ:");
        while (iterator.hasNext()) {
            Dish dish = iterator.next();
            System.out.println(dish);
        }
    }

    private void addDishToActiveOrder() {
        showDishes();
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
            updateOrder();
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

    private void makeOrder() {
        if (activeOrder.getStatus() == OrderStatus.NEW) {
            activeOrder.setStatus(OrderStatus.IN_PROGRESS);
            kitchen.addOrder(activeOrder, this::updateOrder);
        } else {
            view.showErrorMessage("Невозможно оформить заказ");
        }
    }

    private void updateOrder() {
        dishesMenu.updateOrder(activeOrder);
    }

    private void loadOrder() {
        activeOrder = dishesMenu.getActiveOrderByUserId(currentUser.getId());
    }
}
