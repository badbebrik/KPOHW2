package org.example.repository;

import org.example.model.Review;

public interface ReviewRepository {
    void addReview(Review review);

    int getReviewNumber();

    void update();

}
