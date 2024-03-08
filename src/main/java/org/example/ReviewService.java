package org.example;

import org.example.model.Order;

public class ReviewService {

    Order order;
    public ReviewService(Order order) {
        this.order = order;
    }

    public void run() {
        System.out.println("Желаете оставить отзыв о заказе? (да/нет)");
        String answer = Main.scanner.nextLine();
        if (answer.equals("да")) {
            System.out.println("Введите отзыв:");

            String review = Main.scanner.nextLine();
            for (int i = 0; i < order.getDishes().size(); i++) {
                System.out.println("Какую оценку вы бы хотели поставить от 1 до 5 для блюда " + order.getDishes().get(i).getName() + "?");
                int rating = Main.scanner.nextInt();

                Main.scanner.nextLine();
            }
        }

    }
}
