package org.example.view;

import org.example.model.Dish;
import org.example.repository.DishesMenuRepository;
import org.example.repository.MoneyStorage;
import org.example.service.StatisticsCalculator;


import java.text.DecimalFormat;
import java.util.List;

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
        System.out.println("6. Установить время приготовления");
        System.out.println("7. Установить цену");
        System.out.println("8. Показать кассу");
        System.out.println("9. Показать отзывы");
        System.out.println("10. Выход");
    }

    @Override
    public void showVisitorMenu() {
        showMessageColored("Меню посетителя:", ConsoleColors.ANSI_BLUE);
        System.out.println("1. Показать меню");
        System.out.println("2. Создать заказ");
        System.out.println("3. Добавить блюдо в заказ");
        System.out.println("4. Показать заказ");
        System.out.println("5. Оформить заказ");
        System.out.println("6. Показать статус заказа");
        System.out.println("7. Оплатить заказ");
        System.out.println("8. Отменить заказ");
        System.out.println("9. Выход");
    }


    @Override
    public void showErrorMessage(String message) {
        showMessageColored("Ошибка: " + message, ConsoleColors.ANSI_RED);
    }

    @Override
    public void showMenuItemsAdmin(DishesMenuRepository dishesMenu) {
        showMessageColored("Меню Ресторана:", ConsoleColors.ANSI_BLUE);
        dishesMenu.forEach(System.out::println);
    }

    @Override
    public void showMenuItemsVisitor(DishesMenuRepository dishesMenu) {
        DecimalFormat df = new DecimalFormat("#.##");
        showMessageColored("Меню Ресторана:", ConsoleColors.ANSI_BLUE);
        dishesMenu.forEach(dish -> {
            double rating = dish.getRating();
            String formattedRating = rating != 0 ? df.format(rating) + "/5" : "Нет оценок";
            System.out.println(dish.getId() + ". " +  dish.getName() + " (" + dish.getDescription() + ")" + " - " + dish.getPrice() + " руб." + " - Рейтинг: " + formattedRating);
        });
    }

    @Override
    public void showStatistics(StatisticsCalculator statisticsCalculator) {
        DecimalFormat df = new DecimalFormat("#.##"); // Формат с двумя знаками после запятой

        showMessageColored("Статистика:", ConsoleColors.ANSI_BLUE);
        System.out.println("Количество заказов за весь период: " + statisticsCalculator.getTotalOrderNumber());
        System.out.println("Количество заказов за этот сеанс работы: " + statisticsCalculator.getTotalOrderSessionCounter());
        System.out.println("Количество отзывов: " + statisticsCalculator.getTotalReviewNumber());

        System.out.println("Статистика по блюдам:");
        System.out.println("Самые популярные блюда: ");
        List<Dish> popularDishes = statisticsCalculator.getMostPopularDish();
        popularDishes.forEach(dish -> System.out.println(dish.getName() + " - " + df.format(dish.getRating())));
        System.out.println("Средняя оценка блюд: " + df.format(statisticsCalculator.getAverageRating()));
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
