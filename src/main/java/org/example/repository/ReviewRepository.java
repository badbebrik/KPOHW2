package org.example.repository;

import org.example.model.Review;

import java.util.Iterator;

public interface ReviewRepository extends Iterable<Review> {
    void addReview(Review review);

    int getReviewNumber();

    void update();
}
