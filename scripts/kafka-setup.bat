@echo off
REM Kafka Setup Script for Windows
REM Creates topics and tests connectivity

set KAFKA_CONTAINER=kafka
set BOOTSTRAP_SERVERS=localhost:9092

echo 🚀 Setting up Kafka topics...

REM Create topics
echo Creating activity-events topic...
docker exec %KAFKA_CONTAINER% kafka-topics --create --bootstrap-server %BOOTSTRAP_SERVERS% --topic activity-events --partitions 3 --replication-factor 1 --if-not-exists

echo Creating notification-events topic...
docker exec %KAFKA_CONTAINER% kafka-topics --create --bootstrap-server %BOOTSTRAP_SERVERS% --topic notification-events --partitions 3 --replication-factor 1 --if-not-exists

REM List topics
echo 📋 Current topics:
docker exec %KAFKA_CONTAINER% kafka-topics --list --bootstrap-server %BOOTSTRAP_SERVERS%

echo ✅ Kafka setup complete!
echo 🌐 Kafka UI available at: http://localhost:8080
echo 📡 Kafka broker available at: localhost:9092