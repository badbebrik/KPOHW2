package org.example;

import org.example.model.Order;
import org.example.model.Review;

import java.util.Iterator;
import java.util.List;

public class ReviewRepo implements Iterable<Review> {
    List<Review> reviews;

    public ReviewRepo() {
        reviews = DataBaseHandler.loadReviews();
    }

    public void updateReview(Order activeOrder) {
        DataBaseHandler.updateOrder(activeOrder);
        update();
    }

    public void update() {
        reviews = DataBaseHandler.loadReviews();
    }

    public void addReview(int id, String review, int id1) {
        DataBaseHandler.saveReview(Review.builder().id(id).review(review).orderId(id1).build());
        update();
    }

    @Override
    public Iterator<Review> iterator() {
        return new ReviewIterator();
    }

    private class ReviewIterator implements Iterator<Review> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < reviews.size();
        }

        @Override
        public Review next() {
            return reviews.get(currentIndex++);
        }
    }

    public int getReviewNumber() {
        return reviews.size();
    }
}
