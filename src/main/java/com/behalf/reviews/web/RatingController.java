package com.behalf.reviews.web;

import com.behalf.delta.entity.UserInformation;
import com.behalf.delta.service.AuthService;
import com.behalf.reviews.models.Rating;
import com.behalf.reviews.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final AuthService authService;

    @PostMapping("/api/ratings")
    public ResponseEntity<Rating> submitRating(@RequestBody Rating rating) {
        if (rating.getUserId() == null || rating.getUserType() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Validate scores and categories:
        if (rating.getRatingDetails() == null || rating.getRatingDetails().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        boolean hasOverall = rating.getRatingDetails().stream()
                .anyMatch(rd -> "overall".equals(rd.getCategory()) && rd.getScore() >= 1 && rd.getScore() <= 5);

        if (!hasOverall) {
            return ResponseEntity.badRequest().body(null);
        }

        for (var detail : rating.getRatingDetails()) {
            if (detail.getScore() < 1 || detail.getScore() > 5) {
                return ResponseEntity.badRequest().body(null);
            }
            detail.setRating(rating); // set bidirectional link
        }

        Rating saved = ratingService.saveRating(rating);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/public/api/ratings")
    public ResponseEntity<List<Rating>> getRatings(@AuthenticationPrincipal OidcUser oidcUser) {
        // You can infer userType inside service if needed or ignore it
        UserInformation userInformation = authService.getCurrentUser(oidcUser);
        List<Rating> ratings = ratingService.getRatingsForUser(userInformation.getId());
        return ResponseEntity.ok(ratings);
    }

}
