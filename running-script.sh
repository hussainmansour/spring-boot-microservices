#!/bin/bash

# Define the list of service directories
services=("movie-catalog-service" "ratings-data-service" "discovery-server" "movie-info-service")

# File to store process IDs
pid_file="service_pids.txt"
> "$pid_file" # Clear the file

# Start services and store PIDs
for service in "${services[@]}"; do
    echo "Starting $service..."
    (
        cd "$service" || { echo "Failed to enter $service"; exit 1; }
        mvn spring-boot:run & echo $! >> "../$pid_file"
    )
done

echo "All services started! PIDs saved in $pid_file."

