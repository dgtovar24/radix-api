#!/bin/bash
./mvnw clean package -DskipTests -q
java -Dspring.profiles.active=prod -jar target/*.jar &
PID=$!
sleep 15
curl -s http://localhost:8080/v2/notfound
curl -s http://localhost:8080/v2/actuator/health
curl -s http://localhost:8080/v2/
kill $PID
