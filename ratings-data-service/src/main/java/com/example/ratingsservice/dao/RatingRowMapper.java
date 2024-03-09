package com.example.ratingsservice.dao;

import com.example.ratingsservice.models.Rating;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingRowMapper implements RowMapper<Rating> {
    @Override
    public Rating mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Rating(
                resultSet.getString("movie_id"),
                resultSet.getInt("rating")
        );
    }
}
