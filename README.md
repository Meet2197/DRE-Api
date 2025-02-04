Data Transfer Application:
======================================================================================================================================================================================================================
This is a Spring Boot application that demonstrates data transfer functionalities using Kafka for messaging and integration with the Nextcloud API. It provides a REST API endpoint for uploading CSV data, which gets transferred to a Kafka topic and then forwarded to the Nextcloud API.

1.1 Versions:
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Java: 11

Spring Boot: 2.6.4

Kafka: 3.0.0

Apache HttpClient: 4.5.13

1.2 Prerequisites :
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Java 11 or later installed

Docker and Docker Compose for running Kafka

Maven for building the project

1.3 Installation :
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Clone this repository:

```bash

git clone https://github.com/Meet2197/data-transfer.git
```

Navigate to the project directory:

```bash

cd data-transfer
```

Run Kafka and Zookeeper using Docker Compose:

```bash

docker-compose up -d

```

1.4 Configuration:
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kafka Properties (application.yml)

The application.yml file contains Kafka properties for configuring the Kafka producer.
```bash

yaml

server:

  port: 8080

spring:

  kafka:
  
    bootstrap-servers: localhost:9092
    
    producer:
    
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

1.5 Docker Compose :
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

The docker-compose.yml file defines services for Kafka and Zookeeper.
```bash

yaml

version: '3.7'

services:

  zookeeper:

    image: wurstmeister/zookeeper
    
    container_name: zookeeper
    
    ports:
    
      - "2181:2181"
  
  kafka:
    
    image: wurstmeister/kafka
    
    container_name: kafka
    
    ports:
      - "9092:9092"
    
    environment:
    
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    depends_on:
    
      - zookeeper
```
1.6 Usage
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Build the Spring Boot application using Maven:

```bash
mvn clean package
```

1.7 Run the Spring Boot application:
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

```bash

java -jar target/data-transfer-1.0.0-SNAPSHOT.jar

```

You can now access the API at http://localhost:8080/upload-csv to upload CSV data.

Contributors

Add your name if you contributed to the project

License

This project is licensed under the MIT License.

Feel free to customize this README according to your project's specifics and add more sections as needed.
