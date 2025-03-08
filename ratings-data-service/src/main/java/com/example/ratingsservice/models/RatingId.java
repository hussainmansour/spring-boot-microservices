package com.example.ratingsservice.models;

import java.io.Serializable;
import java.util.Objects;

public class RatingId implements Serializable {
    private String userId;
    private String movieId;

    public RatingId() {}

    public RatingId(String userId, String movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    // Required for proper functioning in JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId that = (RatingId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, movieId);
    }
}
