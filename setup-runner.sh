#!/bin/bash
# Setup script for self-hosted GitHub Actions runner

set -e

RUNNER_DIR="/home/ubuntu/actions-runner"
REPO_URL="https://github.com/diegogzt/radix-api"
RUNNER_NAME="radix-runner"

echo "Generating runner token..."
TOKEN=$(echo '{}' | gh api repos/diegogzt/radix-api/actions/runners/authtoken -X POST -q .token 2>/dev/null)

if [ -z "$TOKEN" ]; then
    echo "ERROR: Could not generate runner token. Make sure GitHub Actions is enabled."
    echo "Go to: https://github.com/diegogzt/radix-api/settings/actions"
    exit 1
fi

cd "$RUNNER_DIR"

echo "Configuring runner..."
./config.sh --url "$REPO_URL" \
    --token "$TOKEN" \
    --name "$RUNNER_NAME" \
    --labels self-hosted \
    --ephemeral \
    --unattended \
    --replace

echo "Starting runner..."
./run.sh &