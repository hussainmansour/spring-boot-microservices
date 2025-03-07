package com.example.grpc.client;

import com.example.grpc.Top10Request;
import com.example.grpc.Top10Response;
import com.example.grpc.Top10ServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) {
        // Create a channel
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
    }
}
