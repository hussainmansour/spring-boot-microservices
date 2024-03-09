package com.example.ratingsservice.dao;

import com.example.ratingsservice.models.Rating;

import java.util.List;

public interface RatingsDAO {
    // get list of ratings for a user per userId
    List<Rating> getRatings(String userId);
}
