package com.example.ratingsservice.resources;

import com.example.ratingsservice.models.Rating;
import com.example.ratingsservice.models.UserRating;
import com.example.ratingsservice.repositories.RatingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingsResource {

    private final RatingRepository ratingRepository;

    public RatingsResource(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    // Get ratings by user ID
    @GetMapping("/{userId}")
    public UserRating getRatingsOfUser(@PathVariable String userId) {
        List<Rating> ratings = ratingRepository.findByUserId(userId);
        return new UserRating(ratings);
    }

    // Add a new rating
    @PostMapping("/add")
    public Rating addRating(@RequestBody Rating rating) {
        return ratingRepository.save(rating);
    }
}
