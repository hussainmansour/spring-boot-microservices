package com.moviecatalogservice.resources;

import com.moviecatalogservice.models.CatalogItem;
import com.moviecatalogservice.models.Movie;
import com.moviecatalogservice.models.Rating;
import com.moviecatalogservice.models.UserRating;
import com.moviecatalogservice.services.MovieInfoService;
import com.moviecatalogservice.services.UserRatingService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.grpc.Top10Request;
import com.example.grpc.Top10Response;
import com.example.grpc.Top10ServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topMovies")
public class Top10CatalogResource {

    private final RestTemplate restTemplate;

    private final MovieInfoService movieInfoService;

    private final UserRatingService userRatingService;

    public Top10CatalogResource(RestTemplate restTemplate,
                                MovieInfoService movieInfoService,
                                UserRatingService userRatingService) {

        this.restTemplate = restTemplate;
        this.movieInfoService = movieInfoService;
        this.userRatingService = userRatingService;
    }

    /**
     * Makes a call to MovieInfoService to get movieId, name and description,
     * Makes a call to RatingsService to get ratings
     * Accumulates both data to create a MovieCatalog
     * @param userId
     * @return CatalogItem that contains name, description and rating
     */
    @RequestMapping("/{userId}")
    public String getCatalog(@PathVariable String userId) {
        System.out.println("Ahmed is testing id: " + userId);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        // Create a stub
        Top10ServiceGrpc.Top10ServiceBlockingStub stub = Top10ServiceGrpc.newBlockingStub(channel);

        // Call the RPC method
        Top10Request request = Top10Request.newBuilder().build();
        Top10Response response = stub.getTop10(request);

        // Print the response
        System.out.println("Response from server: " + response.getMovies(0));

        // Shutdown the channel
        channel.shutdown();
        return "Ahmed is testing top 10 service end point" + "Response from server: " + response.getMovies(0);
    }
}
