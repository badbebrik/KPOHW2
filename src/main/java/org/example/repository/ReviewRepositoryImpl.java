package org.example.repository;

import org.example.database.DataBaseHandler;
import org.example.model.Review;

import java.util.Iterator;
import java.util.List;

public class ReviewRepositoryImpl implements Iterable<Review>, ReviewRepository {
    List<Review> reviews;

    public ReviewRepositoryImpl() {
        reviews = DataBaseHandler.loadReviews();
    }

    @Override
    public void update() {
        reviews = DataBaseHandler.loadReviews();
    }

    @Override
    public void addReview(Review review) {
        DataBaseHandler.saveReview(review);
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

    @Override
    public int getReviewNumber() {
        return reviews.size();
    }
}
