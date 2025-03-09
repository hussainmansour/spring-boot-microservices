package com.moviecatalogservice.services;

import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.example.grpc.Top10Request;
import com.example.grpc.Top10Response;
import com.example.grpc.Top10ServiceGrpc;
import com.example.grpc.ProtoRating;

public class Top10Service {

    public static List<ProtoRating> getTop10Movies() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        // Create a stub
        Top10ServiceGrpc.Top10ServiceBlockingStub stub = Top10ServiceGrpc.newBlockingStub(channel);

        // Call the RPC method
        Top10Request request = Top10Request.newBuilder().build();
        Top10Response response = stub.getTop10(request);

        // Shutdown the channel
        channel.shutdown();
        return response.getRatingsList();
    }
}
