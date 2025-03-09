package com.example.grpc.server;

import java.util.List;
import java.util.Random;

import com.example.databaseConnector.MovieDatabaseConnector;
import com.example.grpc.ProtoRating;
import com.example.grpc.Top10Request;
import com.example.grpc.Top10Response;
import com.example.grpc.Top10ServiceGrpc;
import io.grpc.stub.StreamObserver;

public class Top10ServiceImpl extends Top10ServiceGrpc.Top10ServiceImplBase {
    @Override
    public void getTop10(Top10Request request, StreamObserver<Top10Response> responseObserver) {
        List<ProtoRating> ratings = MovieDatabaseConnector.getTop10Ratings();
        Top10Response.Builder responseBuilder = Top10Response.newBuilder();
        for (ProtoRating rate : ratings) {
            responseBuilder.addRatings(rate);
        }

        // Send the response
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
