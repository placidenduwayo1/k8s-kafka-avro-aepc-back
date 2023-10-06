# Application base microservices
- application base microservices that manage addresses, employees, companies and projects. 
- each business microservices of the application is implemented into clean architecture. 
- each writing event is published and istributed using kafka infrastructure.
## business microservices
- Address-microservice 
- Employee-microservice 
- company-microservice
- Project-microservice

# utility services

## 1. utility microservices
- microservices-config-service: to centralize and distribute all microservices configurations
- microservices-registry-service: to register the name of each service
- gateway-service: a gateway between backend of microservices and front

## 2. kafka infrastructure
- a kafka infrastructure to publish and distribute events.
- each writing event in database (POST, DELETE, UPDATE) is distributed into kafka topics.
- **schema registry** to difine schema for all events and **avro** to serialiaze events sent to topics
- kafdrop is used as UI for managing and exploring kafka events, kafka servers,...
- kafka infrastructure:
  - zookeeper: to manage kafka brokers
  - kafka-server(3 brokers):  to publish events into topics and disbribute events to consumers
  - schema registry: to define and register the schema of the event
  - avro schema:to serialize kafka events
  - kafdrop: a kafka UI
## 3. database
mysql- db for peristing data from business microservices
# unit tests and deploment
- each code unit of business microservices is tested with **JUnit5** and **AssertJ**.
- **Mockito** is used to mock external unit dependency.
- **KafkaContainer** is used  to mock Kafka  containers producer/consumer.
      - [Testcontainers for Java](https://java.testcontainers.org/modules/kafka/).
- each service (business and utility microservice) is deployed in docker image.
- a docker-compose template is used to deploy all images of the application.
- Jenkins is used for continuous integrataion and deployment (CI/CD). After each git commit, Jenkins launch automatically following jobs:
  - a build of each microservice.
  - unit tests of all business microservices and published a report of passed, skipped and fail tests.
  - package each microservice and publish a related jar file.
  - build a docker image of each microservice refering to a dockerfile defined inside microservice.
  - publish docker images in docker registry.
  - run docker images of microservices of application in docker containers.
 
    ### ci/cd summary
![my-ci-cd-flow](https://github.com/placidenduwayo1/k8s-kafka-avro-aepc-back/assets/124048212/b8e87f11-5a79-40c4-a132-9327ae0639dc)

 ### deployed microservices in docker images
 - kafka infrastructure:
  - zookeeper (one instance) for managing kafka brokers
  - kafka server (three instances)
  - kafdrop (one instance) for web UI to view kafka brokers, topics and events produced
- utility microservices:
  - a config service for managing and externalize services configuration
  - a registry service to serve gateway service and business services to registry with their name
  - a gateway service

- business microservices:
  - bs-ms-address
  - bs-ms-employee
  - bs-ms-company
  - bs-ms-project

    
## architecture kafka inside business microservice
- a model is a java bean that is sent as payload using a REST api, a spring service build a kafka message with the model.
- a spring service uses kafka producer to send the kafka message to kafka topic.
- a spring service uses kafka consumer to subscribe to the kafka topic and consumes the message from topics that it sends to another spring service either to persist it in db.
  ### kafka infra summary
![kafka-infrastructure](https://github.com/placidenduwayo1/k8s-kafka-avro-aepc-back/assets/124048212/4cb3738e-718a-466c-9b59-41d4773a1a0b)

- a schema registry (docker image) defines a schema for all events published into kafka topics
- avro uses the defined and registered schema to serialize avents before publishing them into topics


