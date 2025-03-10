package com.moviecatalogservice.resources;

import com.moviecatalogservice.models.CatalogItem;
import com.moviecatalogservice.models.Movie;
import com.moviecatalogservice.models.Rating;
import com.moviecatalogservice.models.UserRating;
import com.moviecatalogservice.services.MovieInfoService;
import com.moviecatalogservice.services.Top10Service;
import com.moviecatalogservice.services.UserRatingService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.grpc.Top10Request;
import com.example.grpc.Top10Response;
import com.example.grpc.Top10ServiceGrpc;
import com.example.grpc.ProtoRating;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topMovies")
public class Top10CatalogResource {

    private final MovieInfoService movieInfoService;

    public Top10CatalogResource(
            MovieInfoService movieInfoService) {

        this.movieInfoService = movieInfoService;
    }

    /**
     * Makes a call to MovieInfoService to get movieId, name and description,
     * Makes a call to RatingsService to get ratings
     * Accumulates both data to create a MovieCatalog
     * 
     * @param userId
     * @return CatalogItem that contains name, description and rating
     */
    @RequestMapping("")
    public List<CatalogItem> getCatalog() {
        List<ProtoRating> ratings = Top10Service.getTop10Movies();
        return ratings.stream().map(movieInfoService::getCatalogItem).collect(Collectors.toList());
    }
}
