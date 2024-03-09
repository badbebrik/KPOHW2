package org.example;

import org.example.model.Dish;
import org.example.model.Order;

public class ReviewService {

    Order order;
    ReviewRepo reviewRepo;
    DishesMenu dishesMenu;

    public ReviewService(Order order, ReviewRepo reviewRepo, DishesMenu dishesMenu) {
        this.order = order;
        this.reviewRepo = reviewRepo;
        this.dishesMenu = dishesMenu;
    }

    public void run() {
        System.out.println("Желаете оставить отзыв о заказе? (да/нет)");
        String answer = Main.scanner.nextLine();
        if (answer.equals("да")) {
            System.out.println("Введите отзыв:");
            String review = Main.scanner.nextLine();
            for (int i = 0; i < order.getDishes().size(); i++) {
                System.out.println("Оцените блюдо " + order.getDishes().get(i).getName() + " от 1 до 5");
                int rating = Main.scanner.nextInt();
                Main.scanner.nextLine();
                Dish assessedDish = order.getDishes().get(i);
                assessedDish.setRating((assessedDish.getRating() * assessedDish.getRatingCount() + rating) / (assessedDish.getRatingCount() + 1));
                assessedDish.setRatingCount(assessedDish.getRatingCount() + 1);
                dishesMenu.updateDishRating(assessedDish);
            }
            reviewRepo.addReview(order.getId(), review, order.getId());
            reviewRepo.update();
            System.out.println("Спасибо за оставленный отзыв!");
        }

    }
}
