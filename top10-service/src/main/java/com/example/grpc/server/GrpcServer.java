package com.example.grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService((BindableService) new Top10ServiceImpl())
                .build();

        System.out.println("gRPC Server started on port 9090...");
        server.start();
        server.awaitTermination();
    }
}