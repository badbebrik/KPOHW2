package org.example.view;

import org.example.ConsoleColors;
import org.example.DishesMenu;
import org.example.MoneyStorage;
import org.example.StatisticsCalculator;
import org.example.model.Order;

public class ConsoleView implements View {

    @Override
    public void showAuthMenu() {
        showMessageColored("Меню авторизации:", ConsoleColors.ANSI_BLUE);
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");
        System.out.println("3. Выход");
    }

    @Override
    public void showRegistrationSuccess(String username) {
        showMessageColored("Регистрация пользователя " + username + " прошла успешно", ConsoleColors.ANSI_GREEN);
    }

    @Override
    public void showAdminMenu() {
        showMessageColored("Меню администратора:", ConsoleColors.ANSI_BLUE);
        System.out.println("1. Добавить блюдо");
        System.out.println("2. Удалить блюдо");
        System.out.println("3. Показать меню");
        System.out.println("4. Статистика");
        System.out.println("5. Установить количество блюдa");
        System.out.println("6. Показать кассу");
        System.out.println("7. Показать отзывы");
        System.out.println("8. Выход");
    }

    @Override
    public void showVisitorMenu() {
        showMessageColored("Меню посетителя:", ConsoleColors.ANSI_BLUE);
        System.out.println("1. Создать заказ");
        System.out.println("2. Посмотреть заказ");
        System.out.println("3. Добавить блюдо в активный заказ");
        System.out.println("4. Показать статус заказа");
        System.out.println("5. Отменить заказ");
        System.out.println("6. Заплатить за заказ");
        System.out.println("7. Оформить заказ");
        System.out.println("8. Выход");
    }


    @Override
    public void showErrorMessage(String message) {
        showMessageColored("Ошибка: " + message, ConsoleColors.ANSI_RED);
    }

    @Override
    public void showMenuItems(DishesMenu dishesMenu) {
        showMessageColored("Меню Ресторана:", ConsoleColors.ANSI_BLUE);
        dishesMenu.forEach(dish -> System.out.println(dish.getName() + " Цена: " + dish.getPrice() + " Доступное количество: " + dish.getQuantity()));
    }

    @Override
    public void showStatistics(StatisticsCalculator statisticsCalculator) {
        statisticsCalculator.showStatistics();
    }

    @Override
    public void showMoneyStorage(MoneyStorage moneyStorage) {
        System.out.println(ConsoleColors.ANSI_BLUE + "Касса:" + ConsoleColors.ANSI_RESET);
        System.out.println("Наличные: " + moneyStorage.getCash());
        System.out.println("Безналичные: " + moneyStorage.getNonCash());
        System.out.println("Всего: " + moneyStorage.getTotalMoney());
    }

    @Override
    public void showMessageColored(String option, String color) {
        System.out.println(color + option + ConsoleColors.ANSI_RESET);
    }
}
