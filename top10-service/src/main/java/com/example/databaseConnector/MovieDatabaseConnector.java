package com.example.databaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.example.grpc.ProtoRating;
import com.example.grpc.ProtoRatingOrBuilder;

public class MovieDatabaseConnector {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/movieratings";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "123456789"; // Replace with your MySQL password

    private static final List<Integer> MOVIE_IDS = Arrays.asList(27205, 157336, 155, 19995, 24428, 293660, 299536, 550,
            118340, 680);

    private static final int NUM_RATINGS_PER_MOVIE = 100;
    private static final Random RANDOM = new Random();

    public static List<ProtoRating> getTop10Ratings() {
        List<Movie> movies = fetchMovies(); // Assuming fetchMovies() is defined elsewhere
        List<ProtoRating> ratings = new ArrayList<>();

        for (Movie movie : movies) {
            ProtoRating tempRating = ProtoRating.newBuilder()
                    .setMovieId(movie.getMovieId())
                    .setRating(movie.getAvgRating())
                    .build();

            ratings.add(tempRating);
        }

        return ratings;
    }

    // Method to fetch movies from the database
    private static List<Movie> fetchMovies() {
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // SQL query
            String sql = "SELECT movie_id, AVG(rating) AS average_rating\n" + //
                    "FROM ratings\n" + //
                    "GROUP BY movie_id\n" + //
                    "ORDER BY average_rating DESC\n" + //
                    "LIMIT 10;";

            // Create a statement
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {

                // Process the result set
                while (resultSet.next()) {
                    String id = resultSet.getString("movie_id");
                    double averageRating = resultSet.getDouble("average_rating");

                    // Create a Movie object and add it to the list
                    Movie movie = new Movie(id, averageRating);
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            e.printStackTrace();
        }

        return movies;
    }

    public static void seedRatingsDatabase() {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO ratings (user_id, movie_id, rating) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int movieId : MOVIE_IDS) {
                    Set<String> uniquePairs = new HashSet<>(); // Stores user_id and movie_id pairs
                    int attempts = 0; // Prevent infinite loops
                    while (uniquePairs.size() < NUM_RATINGS_PER_MOVIE && attempts < 2 * NUM_RATINGS_PER_MOVIE) {
                        int userId = RANDOM.nextInt(1000) + 1; // Random user_id between 1 and 1000
                        String pairKey = userId + "-" + movieId;

                        if (!uniquePairs.contains(pairKey)) {
                            uniquePairs.add(pairKey); // Store the unique user-movie pair
                            int rating = RANDOM.nextInt(5) + 1; // Random rating between 1 and 5

                            statement.setString(1, String.valueOf(userId));
                            statement.setString(2, String.valueOf(movieId));
                            statement.setInt(3, rating);
                            statement.executeUpdate();
                        }
                        attempts++;
                    }
                }
                System.out.println("Dummy data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Movie class to represent a movie record
class Movie {
    private String id;
    private double averageRating;

    public Movie(String id, double rate) {
        this.id = id;
        this.averageRating = rate;
    }

    public String getMovieId() {
        return this.id;
    }

    public double getAvgRating() {
        return this.averageRating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", rate=" + averageRating +
                '}';
    }
}
