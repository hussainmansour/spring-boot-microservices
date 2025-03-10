package com.example.databaseConnector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    private static final String PASSWORD = "Root@12345"; // Replace with your MySQL password

    private static final int NUM_RATINGS_PER_MOVIE = 5;
    private static final Random RANDOM = new Random();
    private static final int NUM_MOVIES_IDS = 200000;

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
                List<String> MOVIE_IDS = readMoviesIds();
                System.out.println("Start creating dataset...");
                int written = 0;
                int batchSize = 100000; 
                connection.setAutoCommit(false);
    
                for (String movieId : MOVIE_IDS) {
                    for (int userId = 0; userId < NUM_RATINGS_PER_MOVIE; userId++) {
                        int rating = RANDOM.nextInt(5) + 1;
                        statement.setString(1, String.valueOf(userId));
                        statement.setString(2, movieId);
                        statement.setInt(3, rating);
                        statement.addBatch(); 
                        written++;
                        if (written % batchSize == 0) {
                            statement.executeBatch();
                            connection.commit(); 
                            System.out.println("Successfully written " + written + " out of " + (MOVIE_IDS.size() * NUM_RATINGS_PER_MOVIE));
                        }
                    }
                }
                statement.executeBatch();
                connection.commit(); 
                System.out.println("Dummy data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readMoviesIds(){
        System.out.println("Reading movies IDs from file...");
        String csvFile = "/home/ahmed-hehsam/level4term2/dataIntensive/labs/lab2/spring-boot-microservices/movies-data/movies_id.csv";
        List<String> moviesIds = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            int toRead = NUM_MOVIES_IDS;
            while ((line = br.readLine()) != null && toRead > 0) {
                moviesIds.add(line);
                toRead--;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moviesIds;
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
