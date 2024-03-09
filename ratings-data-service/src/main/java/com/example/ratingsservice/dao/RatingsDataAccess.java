package com.example.ratingsservice.dao;

import com.example.ratingsservice.models.Rating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RatingsDataAccess implements RatingsDAO {
    private final JdbcTemplate jdbcTemplate;

    public RatingsDataAccess(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Rating> getRatings(String userId) {
        // return the row mapper to match expected return data type
        String sql = """
                SELECT rating.movie_id, rating.rating FROM rating
                WHERE rating.user_id = ?
                LIMIT 1000;
                """;
        return jdbcTemplate.query(sql, new RatingRowMapper(), userId);
    }
}
