package com.example.databaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDatabaseConnector {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/test_movies";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "Root@12345"; // Replace with your MySQL password

    public static List<String> getMoviesNames(){
        List<Movie> movies = fetchMovies();
        List<String> names = new ArrayList<>();

        for (Movie movie : movies) {
            names.add(movie.getName());
        }
        return names;
    }

    // Method to fetch movies from the database
    private static List<Movie> fetchMovies() {
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // SQL query
            String sql = "SELECT * FROM movies;";

            // Create a statement
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                // Process the result set
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double rate = resultSet.getDouble("rate");

                    // Create a Movie object and add it to the list
                    Movie movie = new Movie(id, name, rate);
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
            e.printStackTrace();
        }

        return movies;
    }
}

// Movie class to represent a movie record
class Movie {
    private int id;
    private String name;
    private double rate;

    public Movie(int id, String name, double rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                '}';
    }
}
