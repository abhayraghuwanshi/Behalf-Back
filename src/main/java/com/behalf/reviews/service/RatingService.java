package com.behalf.reviews.service;


import com.behalf.reviews.models.Rating;
import com.behalf.reviews.repo.RatingRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForUser(Long userId) {
        return ratingRepository.findByUserId(userId);
    }
}
