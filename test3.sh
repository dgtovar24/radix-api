#!/bin/bash
java -Dspring.profiles.active=prod -jar target/*.jar > app3.log 2>&1 &
PID=$!
sleep 12
curl -s http://localhost:8080/v2/
echo ""
curl -s http://localhost:8080/v2/actuator/health
echo ""
kill $PID
