package org.example.view;

import org.example.DishesMenu;
import org.example.model.Order;

public class ConsoleView implements View {

    @Override
    public void showAuthMenu() {
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");
        System.out.println("3. Выход");
    }

    @Override
    public void showRegistrationSuccess(String username) {
        System.out.println("Пользователь " + username + " успешно зарегистрирован");
    }

    @Override
    public void showAdminMenu() {
        System.out.println("1. Добавить блюдо");
        System.out.println("2. Удалить блюдо");
        System.out.println("3. Показать меню");
        System.out.println("4. Статистика");
        System.out.println("5. Увеличить количество блюдa");
        System.out.println("6. Выход");
    }

    @Override
    public void showVisitorMenu() {
        System.out.println("1. Создать заказ");
        System.out.println("2. Посмотреть заказ");
        System.out.println("3. Добавить блюдо в активный заказ");
        System.out.println("4. Показать статус заказа");
        System.out.println("5. Отменить заказ");
        System.out.println("6. Заплатить за заказ");
        System.out.println("7. Сделать заказ");
        System.out.println("7. Выход");
    }


    @Override
    public void showErrorMessage(String message) {
        System.out.println("Ошибка: " + message);
    }

    @Override
    public void showMenuItems(DishesMenu dishesMenu) {
        System.out.println("Меню:");
        dishesMenu.forEach(dish -> System.out.println(dish.getName() + " Цена: " + dish.getPrice() + " Доступное количество: " + dish.getQuantity()));
    }

    @Override
    public void showOrderItems(Order order) {

    }

    @Override
    public void showOrderSuccess() {

    }

    @Override
    public void showOrderCancel() {

    }

    @Override
    public void showOrderDone(String id) {

    }

    @Override
    public void showOrderPaid(String id) {

    }

    @Override
    public void showPaymentMenu() {

    }

    @Override
    public void showStatistics() {

    }
}
