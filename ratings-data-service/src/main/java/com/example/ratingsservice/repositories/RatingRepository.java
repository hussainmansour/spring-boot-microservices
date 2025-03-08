package com.example.ratingsservice.repositories;

import com.example.ratingsservice.models.Rating;
import com.example.ratingsservice.models.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    List<Rating> findByUserId(String userId);
}
