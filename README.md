# Kafka gRPC Spring Project 

This service manages data, using **Kafka** for messaging and **gRPC** for remote procedure calls. It provides REST endpoints to query data with filtering capabilities.  

---

## Features  
- **Kafka Integration**: Listens to events on requested topic and stores the data in a MySQL database.
- **gRPC Support**: Interacts with the service and stores recieved data in MySQL database.
- **REST API with Filtering**:
  - `/stock`: Displays coffee stock, filtered by origin country and/or coffee sort.
  - `/loss-percentage`: Returns the average loss percentage after roasting, filtered by team ID and/or origin country. If no matching data exists, returns an error message.
---
## Technologies Used
- **Kafka**
- **gRPC**
- **Spring Boot**
- **Flyway**
- **Hibernate**
- **Testcontainers**
- **JUnit 5**
- **Swagger**

---
## Tests
- **Kafka Tests**: Kafka listener is tested using Testcontainers to ensure messages are processed and saved to the MySQL DB Testcontainer.

- **gRPC Tests**: gRPC service is tested unsing Testcontainers to validate communication and recieved data is saved to the MySQL DB Testcontainer.
