#!/bin/bash
./mvnw clean package -DskipTests -q
java -jar target/*.jar > app4.log 2>&1 &
PID=$!
sleep 15
echo "--- GET /v2 ---"
curl -s -i http://localhost:8080/v2
echo -e "\n--- GET /v2/ ---"
curl -s -i http://localhost:8080/v2/
kill $PID
