package com.example.ratingsservice.services;

import com.example.ratingsservice.dao.RatingsDAO;
import com.example.ratingsservice.models.Rating;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final RatingsDAO ratingsDataAccess;

    public RatingService(RatingsDAO ratingsDataAccess) {
        this.ratingsDataAccess = ratingsDataAccess;
    }

    public List<Rating> getMoviesRatingByUser(String userId) {
        return ratingsDataAccess.getRatings(userId);
    }
}
