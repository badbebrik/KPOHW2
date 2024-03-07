package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.DishesMenu;
import org.example.Main;
import org.example.MenuI;
import org.example.OrderRepo;
import org.example.model.Dish;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.User;
import org.example.view.ConsoleView;

public class VisitorMenu implements MenuI {

    @Setter
    @Getter
    private User currentUser = null;
    private DishesMenu dishesMenu;
    private Kitchen kitchen;
    private OrderRepo orderRepo;
    private Order activeOrder;
    private final ConsoleView view;


    public VisitorMenu(User user, ConsoleView view, DishesMenu dishesMenu, Kitchen kitchen, OrderRepo orderRepo) {
        this.view = view;
        this.dishesMenu = dishesMenu;
        this.kitchen = kitchen;
        this.currentUser = user;
        this.orderRepo = orderRepo;
        activeOrder = orderRepo.getActiveOrderByUserId(currentUser.getId());
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
        activeOrder.setUserId(currentUser.getId());
        orderRepo.addOrder(activeOrder);
        loadOrder();
    }

    private void showDishes() {
        dishesMenu.showDishes();
    }

    private void showOrder() {
        if (activeOrder == null) {
            System.out.println("У вас нет активных заказов");
            return;
        }
        System.out.println("Ваш заказ:");
        for (Dish dish : activeOrder.getDishes()) {
            System.out.println(dish);
        }
    }

    private void addDishToActiveOrder() {
        if (activeOrder == null) {
            System.out.println("У вас нет активных заказов");
            return;
        }

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
        if (activeOrder == null) {
            System.out.println("У вас нет активных заказов");
            return;
        }

        System.out.println("Статус заказа: " + activeOrder.getStatus());
    }

    private void cancelOrder() {
        if (activeOrder == null) {
            System.out.println("У вас нет активных заказов");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.DONE) {
            System.out.println("Невозможно отменить выполненный заказ");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.PAID) {
            System.out.println("Невозможно отменить оплаченный заказ");
            return;
        }

        for (Dish dish : activeOrder.getDishes()) {
            dishesMenu.increaseDishQuantity(dish.getId());
        }

        activeOrder.setCancelled(true);
        orderRepo.removeOrder(activeOrder);

        activeOrder = null;
    }

    private void payForOrder() {
        if (activeOrder == null) {
            System.out.println("У вас нет активных заказов");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.PAID) {
            System.out.println("Заказ уже оплачен");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.DONE) {
            activeOrder.setStatus(OrderStatus.PAID);
        }

        updateOrder();
    }

    private void makeOrder() {
        if (activeOrder.getStatus() == OrderStatus.NEW) {
            activeOrder.setStatus(OrderStatus.IN_PROGRESS);
            updateOrder();
            kitchen.addOrder(activeOrder, this::updateOrder);
        } else {
            view.showErrorMessage("Невозможно оформить заказ");
        }
    }

    private void updateOrder() {
        orderRepo.updateOrder(activeOrder);
    }

    private void loadOrder() {
        activeOrder = orderRepo.getActiveOrderByUserId(currentUser.getId());
    }
}
