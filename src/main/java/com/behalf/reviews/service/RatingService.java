package com.behalf.reviews.service;


import com.behalf.delta.entity.dto.PageResponse;
import com.behalf.reviews.models.Rating;
import com.behalf.reviews.repo.RatingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public PageResponse<Rating> getRatingsForUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageResponse<>(ratingRepository.findByUserId(userId, pageable));
    }
}
