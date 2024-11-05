#!/bin/bash

# Check if a file path was provided as an argument
if [ -z "$1" ]; then
  echo "Usage: $0 path_to_env_file"
  exit 1
fi

ENV_FILE="$1"

# Check if the specified .env file exists
if [ ! -f "$ENV_FILE" ]; then
  echo "Error: .env file not found at $ENV_FILE"
  exit 1
fi

# Export variables from the specified .env file
export $(grep -v '^#' "$ENV_FILE" | xargs)

# Run Maven command
mvn install
