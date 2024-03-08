package org.example;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Review;

import java.util.List;

public class ReviewRepo {
    List<Review> reviews;

    public ReviewRepo() {
        reviews = DataBaseHandler.loadReviews();
    }

    public void addReview(Order activeOrder) {
        DataBaseHandler.addOrder(activeOrder);
        update();
    }

    public void updateReview(Order activeOrder) {
        DataBaseHandler.updateOrder(activeOrder);
        update();
    }

    public void update() {
        reviews = DataBaseHandler.loadReviews();
    }
}
