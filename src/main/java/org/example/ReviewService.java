package org.example;

import org.example.model.Order;

public class ReviewService {

    Order order;
    ReviewRepo reviewRepo;
    public ReviewService(Order order, ReviewRepo reviewRepo) {
        this.order = order;
        this.reviewRepo = reviewRepo;
    }

    public void run() {
        System.out.println("Желаете оставить отзыв о заказе? (да/нет)");
        String answer = Main.scanner.nextLine();
        if (answer.equals("да")) {
            System.out.println("Введите отзыв:");
            String review = Main.scanner.nextLine();
            for (int i = 0; i < order.getDishes().size(); i++) {
                Main.scanner.nextLine();
            }
        }

    }
}
