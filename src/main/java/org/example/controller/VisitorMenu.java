package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.*;
import org.example.model.Dish;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.User;
import org.example.view.ConsoleView;

public class VisitorMenu implements MenuI {

    @Setter
    @Getter
    private User currentUser = null;
    private final DishesMenu dishesMenu;
    private final Kitchen kitchen;
    private final OrderRepo orderRepo;
    private Order activeOrder;
    private final ConsoleView view;

    private final MoneyStorage moneyStorage;


    private final ReviewRepo reviewRepo;


    public VisitorMenu(User user, ConsoleView view, DishesMenu dishesMenu, Kitchen kitchen, OrderRepo orderRepo, MoneyStorage moneyStorage, ReviewRepo reviewRepo) {
        this.view = view;
        this.dishesMenu = dishesMenu;
        this.kitchen = kitchen;
        this.currentUser = user;
        this.orderRepo = orderRepo;
        activeOrder = orderRepo.getActiveOrderByUserId(currentUser.getId());
        this.moneyStorage = moneyStorage;
        this.reviewRepo = reviewRepo;
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
                case 1 -> createOrder();
                case 2 -> showOrder();
                case 3 -> addDishToActiveOrder();
                case 4 -> showActiveOrdersStatus();
                case 5 -> cancelOrder();
                case 6 -> payForOrder();
                case 7 -> makeOrder();
                case 8 -> {
                    return;
                }
                default -> view.showErrorMessage("Некорректный ввод. Введите число от 1 до 8");
            }
        }
    }

    private void createOrder() {
        if (activeOrder != null && activeOrder.getStatus() != OrderStatus.PAID) {

            if (activeOrder.getStatus() == OrderStatus.NEW) {
                view.showErrorMessage("У вас уже есть новый заказ. Добавьте блюда в заказ и оформите его.");
                return;
            }

            if (activeOrder.getStatus() == OrderStatus.DONE) {
                view.showErrorMessage("Вы не оплатили прошлый заказ. Оплатите и создайте новый.");
                return;
            }

            if (activeOrder.getStatus() == OrderStatus.IN_PROGRESS) {
                view.showErrorMessage("Ваш заказ уже готовится. Дождитесь его выполнения.");
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

        if (activeOrder.getDishes().isEmpty()) {
            System.out.println("Ваш заказ пуст");
            return;
        }

        System.out.println("Ваш заказ:");
        for (Dish dish : activeOrder.getDishes()) {
            System.out.println("Блюдо: " + dish.getName() + " Цена: " + dish.getPrice() + " Время приготовления: " + dish.getTimeToCook() + "мин");
        }
    }

    private void addDishToActiveOrder() {
        if (activeOrder == null) {
            System.out.println("У вас нет активных заказов");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.DONE) {
            System.out.println("Невозможно добавить блюдо в выполненный заказ.");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.PAID) {
            System.out.println("Невозможно добавить блюдо в оплаченный заказ.");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.IN_PROGRESS) {
            System.out.println("Невозможно добавить блюдо в готовящийся заказ.");
            return;
        }

        System.out.println("Добавление блюда в заказ:");
        showDishes();
        System.out.println("Введите id блюда:");
        int id;
        try {
            id = Main.scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Некорректный ввод");
            return;
        }

        if (dishesMenu.getDishById(id) != null) {
            if (dishesMenu.getDishById(id).getQuantity() == 0) {
                System.out.println("Блюдо закончилось");
                return;
            }

            activeOrder.addDish(dishesMenu.getDishById(id));
            dishesMenu.decreaseDishQuantity(id);
            orderRepo.updateOrder(activeOrder);
        } else {
            System.out.println("Блюда с таким id не существует");
        }

    }

    private void showActiveOrdersStatus() {
        if (activeOrder == null) {
            System.out.println("У вас нет активных заказов");
            return;
        }

        String status = "";
        switch (activeOrder.getStatus()) {
            case NEW -> status = "Создан";
            case IN_PROGRESS -> status = "Готовится";
            case DONE -> status = "Выполнен";
            case PAID -> status = "Оплачен";
        }

        System.out.println("Статус заказа: " + status);
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

        kitchen.cancelOrder(activeOrder);
        orderRepo.removeOrder(activeOrder);
        activeOrder = null;

        view.showMessageColored("Заказ отменен", ConsoleColors.ANSI_ORANGE);
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

        if (activeOrder.getStatus() == OrderStatus.NEW || activeOrder.getStatus() == OrderStatus.IN_PROGRESS) {
            System.out.println("Заказ еще не готов. Оплатите заказ после получения.");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.DONE) {
            System.out.println("Ваш чек: ");
            for (Dish dish : activeOrder.getDishes()) {
                System.out.println("Блюдо: " + dish.getName() + " Цена: " + dish.getPrice());
            }
            System.out.println("Итого: " + activeOrder.getTotalPrice());

            System.out.println("Как вы хотите оплатить заказ?");
            System.out.println("1. Наличными");
            System.out.println("2. Картой");
            int choice = Main.scanner.nextInt();
            Main.scanner.nextLine();
            if (choice == 1) {
                moneyStorage.addCash(activeOrder.getTotalPrice());
            } else if (choice == 2) {
                moneyStorage.addNonCash(activeOrder.getTotalPrice());
            } else {
                System.out.println("Некорректный ввод");
                return;
            }

            System.out.println("Заказ успешно оплачен");

            ReviewService reviewService = new ReviewService(activeOrder, reviewRepo, dishesMenu);
            reviewService.run();
        }

        activeOrder.setStatus(OrderStatus.PAID);
        updateMoneyStorage();
        orderRepo.updateOrder(activeOrder);
    }

    private void updateMoneyStorage() {
        moneyStorage.updateMoneyStorage();
    }

    private void makeOrder() {
        if (activeOrder == null) {
            System.out.println("У вас нет активных заказов");
            return;
        }

        if (activeOrder.getDishes().isEmpty()) {
            System.out.println("Не удалось оформить заказ. Ваш заказ пуст. Добавьте блюда в заказ и повторите попытку.");
            return;
        }

        if (activeOrder.getStatus() == OrderStatus.NEW) {
            activeOrder.setStatus(OrderStatus.IN_PROGRESS);
            orderRepo.updateOrder(activeOrder);
            System.out.println("Ваш заказ принят в обработку и готовится");
            kitchen.addOrder(activeOrder);
        } else {
            view.showErrorMessage("Невозможно оформить заказ");
        }
    }

    private void loadOrder() {
        activeOrder = orderRepo.getActiveOrderByUserId(currentUser.getId());
    }
}
