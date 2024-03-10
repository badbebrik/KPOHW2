package org.example.service;

import org.example.model.Review;
import org.example.repository.DishesMenuRepository;
import org.example.repository.ReviewRepository;
import org.example.view.ConsoleColors;
import org.example.Main;
import org.example.model.Dish;
import org.example.model.Order;
import org.example.view.View;

public class ReviewService {

    private final Order order;
    private final ReviewRepository reviewRepo;
    private final DishesMenuRepository dishesMenu;

    private final View view;

    public ReviewService(Order order, ReviewRepository reviewRepo, DishesMenuRepository dishesMenu, View view) {
        this.order = order;
        this.reviewRepo = reviewRepo;
        this.dishesMenu = dishesMenu;
        this.view = view;
    }

    public void run() {
        System.out.println("Желаете оставить отзыв о заказе? (да/нет)");
        String answer = Main.scanner.nextLine();
        if (answer.equals("да")) {
            System.out.println("Введите отзыв:");
            String review = Main.scanner.nextLine();
            for (int i = 0; i < order.getDishes().size(); i++) {
                System.out.println("Оцените блюдо " + order.getDishes().get(i).getName() + " от 1 до 5");
                int rating;
                try {
                    rating = Main.scanner.nextInt();
                    if (rating < 1 || rating > 5) {
                        view.showErrorMessage("Некорректный ввод. Введите число от 1 до 5");
                        Main.scanner.nextLine();
                        i--;
                        continue;
                    }
                } catch (Exception e) {
                    view.showErrorMessage("Некорректный ввод. Введите число от 1 до 5");
                    Main.scanner.nextLine();
                    i--;
                    continue;
                }
                Main.scanner.nextLine();
                Dish assessedDish = order.getDishes().get(i);
                assessedDish.setRating((assessedDish.getRating() * assessedDish.getRatingCount() + rating) / (assessedDish.getRatingCount() + 1));
                assessedDish.setRatingCount(assessedDish.getRatingCount() + 1);
                dishesMenu.updateDishRating(assessedDish);
            }

            Review reviewToAdd = Review.builder()
                    .review(review)
                    .orderId(order.getId())
                    .build();

            reviewRepo.addReview(reviewToAdd);
            reviewRepo.update();
            view.showMessageColored("Спасибо за оставленный отзыв!", ConsoleColors.ANSI_GREEN);
        }

    }
}
