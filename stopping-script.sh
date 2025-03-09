#!/bin/bash

pid_file="service_pids.txt"

if [ ! -f "$pid_file" ]; then
    echo "No PID file found. Are services running?"
    exit 1
fi

# Read PIDs from the file and kill them
while read -r pid; do
    echo "Stopping process $pid..."
    kill "$pid" 2>/dev/null
done < "$pid_file"

# Cleanup
rm -f "$pid_file"

echo "All services stopped!"

