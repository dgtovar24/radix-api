#!/bin/bash
docker network connect dokploy-network radix-api || true
docker restart radix-api
