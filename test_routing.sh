#!/bin/bash
./mvnw clean package -DskipTests -q
java -Dspring.profiles.active=prod -jar target/*.jar > app_routing.log 2>&1 &
PID=$!
sleep 15
echo "--- GET /v2 ---"
curl -s -i http://localhost:8080/v2
echo -e "\n--- GET /v2/ ---"
curl -s -i http://localhost:8080/v2/
kill $PID
