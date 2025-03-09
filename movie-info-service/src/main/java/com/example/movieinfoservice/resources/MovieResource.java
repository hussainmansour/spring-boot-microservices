package com.example.movieinfoservice.resources;

import com.example.movieinfoservice.models.Movie;
import com.example.movieinfoservice.models.JpaMovie;
import com.example.movieinfoservice.models.MovieSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.movieinfoservice.repositories.MovieRepository;
import com.example.movieinfoservice.repositories.JpaMovieRepository;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    private RestTemplate restTemplate;
    private MovieRepository movieRepository;
    private JpaMovieRepository jpaMovieRepository;
    private Random random = new Random();

    public MovieResource(RestTemplate restTemplate, MovieRepository movieRepository, JpaMovieRepository jpaMovieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
        this.jpaMovieRepository = jpaMovieRepository;
    }

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {
        // check the mongoDB cache
        Optional<Movie> cachedMovie = movieRepository.findById(movieId);
        if (cachedMovie.isPresent()) {
            return cachedMovie.get();
        }

        try {
            Thread.sleep(500 + random.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Optional<JpaMovie> dbResult = jpaMovieRepository.findById(movieId);
        JpaMovie jpamovie = dbResult.orElse(null);
        Movie movie = new Movie(jpamovie.getMovieId(), jpamovie.getName(), jpamovie.getDescription());

        // Get the movie info from TMDB
//        final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
//        MovieSummary movieSummary = restTemplate.getForObject(url, MovieSummary.class);
//        Movie movie = new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());

        // cache the result in mongoDB
        movieRepository.save(movie);
        return movie;
    }
}
