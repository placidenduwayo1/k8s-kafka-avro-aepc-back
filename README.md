# Application base microservices
- application base microservices that manage addresses, employees, companies and projects. 
- each business microservices of the application is implemented into clean architecture. 
- each writing event is published and istributed using kafka infrastructure.
## business microservices
- Address-microservice 
- Employee-microservice 
- company-microservice
- Project-microservice

# utility tools

## 1. utility microservices
- microservices-config-service: to centralize and distribute all microservices configurations
- microservices-registry-service: to register the name of each service
- gateway-service: a gateway between backend of microservices and front

## 2. kafka infrastructure
- a kafka infrastructure to publish and distribute events.
- each writing event in database (POST, DELETE, UPDATE) is distributed into kafka topics.
- **schema registry** to difine schema for all events and **avro** to serialiaze events sent to topics
- **kafdrop** is used as UI for managing and exploring kafka events, kafka servers,...
- kafka infrastructure:
  - zookeeper: to manage kafka brokers
  - kafka-server(3 brokers), **kafka-broker-1**, **kafka-broker-2**, **kafka-broker-3**: publish events into topics and disbribute events to consumers
  - schema registry: defines and register a common schema of the all events
  - avro schema:to serialize kafka events
  - kafdrop: a kafka UI

## 3. database
mysql database docker image for peristing data from business microservices
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
  - kafdrop (one instance) for web UI for monitoring kafka brokers, topics and events produced
- utility microservices:
  - a config service for managing and externalize services configuration
  - a registry service to serve gateway service and business services to registry with their name
  - a gateway service

- business microservices:
  - bs-ms-address
  - bs-ms-employee
  - bs-ms-company
  - bs-ms-project

### containers orchestration with Kubernetes
all containers of the application are deployed and orchestrated in local minikube cluster
- database: mysql-db
- zookeeper
- kafka servers: kafka-broker-1, kafka-broker-2,kafka-broker-3
- schema-regsitry
- kafdrop UI
- utility services containers: config-service, gateway-service
- business-microservices containers: bs-ms-address, bs-ms-employee, bs-ms-company, bs-ms-project

the folder **./K8s-Containers-Ochrestr** contains k8s containers deployment

    
## architecture kafka inside business microservice
- a model is a java bean that is sent as payload using a REST api, a spring service build a kafka message with the model.
- a spring service uses kafka producer to send the kafka message to kafka topic.
- a spring service uses kafka consumer to subscribe to the kafka topic and consumes the message from topics that it sends to another spring service either to persist it in db.
  ### kafka infra summary
![kafka-infrastructure](https://github.com/placidenduwayo1/k8s-kafka-avro-aepc-back/assets/124048212/4cb3738e-718a-466c-9b59-41d4773a1a0b)

- a schema registry (docker image) defines a schema for all events published into kafka topics
- avro uses the defined and registered schema to serialize avents before publishing them into topics

# general architecture of the project
![k8s-kafka-avro-aepc-clean-archi](https://github.com/placidenduwayo1/k8s-kafka-avro-aepc-back/assets/124048212/91bf9102-e790-448e-9988-3b5ba37ae7b6)

- To access to backend business microservices, the client goes through a ***gateway-service*** pod (the ***gateway-service-pod*** is exposed by the service with same name) and indicates the name of service that exposes the pod he wants to consume as following:
  - ```http://gateway-ip:k8s-generated-port/service-name-exposing-pod/endpoint```
- For instance, if the client wants to get all registrated addresses (k8s-kafka-avro-aepc-bs-ms-address pod): 
  - ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-address/addresses```

  here, ***k8s-kafka-avro-aepc-bs-ms-address*** service exposes the pod k8s-kafka-avro-aepc-bs-ms-address that have de same name as its service

  # address microservice pod

  list of **endpoints** exposed by k8s-kafka-avro-aepc-bs-ms-address pod:
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-address```
    - [POST]```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-address/addresses```
       
      payload {
        num:int value
        street: string value
        poBox: int value
        city: string value
        coutry: string value
      }

    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-address/addresses```
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-address/addresses/id/{value of address id}```
    - [DELETE] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-address/addresses/id/{value of address id}```
    - [PUT] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-address/addresses/id/{value of address id}```
      ```payload {
        num:int value
        street: string value
        poBox: int value
        city: string value
        coutry: string value
      }```

# company microservice pod

  list of **endpoints** exposed by k8s-kafka-avro-aepc-bs-ms-company pod:
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-company```
    - [POST]```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-company/companies```
      ```payload {
        name: string value
        agency: int value
        type: string value
      }```
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-company/companies```
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-company/addresses/id/{value of company id}```
    - [DELETE] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-company/companies/{value of company id}```
    - [PUT] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-company/companies/{value of company id}```
      ```payload {
        name: string value
        agency: int value
        type: string value
      }```

# employee microservice pod

  list of **endpoints** exposed by k8s-kafka-avro-aepc-bs-ms-employee pod:
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-employee```
    - [POST]```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-employee/employees```
      ```payload {
        firstname: string value
        lastname: string value
        state: string value
        type: string value
        address-id: string value
      }```
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-employee/employees```
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-employee/employees/{value of employee id}```
    - [DELETE] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-employee/employees/{value of employee id}```
    - [PUT] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-employee/employees/{value of employee id}```
      ```payload {
        firstname: string value
        lastname: string value
        state: string value
        type: string value
        address-id: string value
      }```
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-employee/employees/addresses/{value of address id}```: employees living at given address

 for [POST] and [PUT], ***address-id**, employee ms sends request to address ms to ask address related to address-id. a resilience is managed if address ms is down

# project microservice pod

  list of **endpoints** exposed by k8s-kafka-avro-aepc-bs-ms-project pod:
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-project```
    - [POST]```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-project/projects```
      ```payload {
        nale: string value
        description: string value
        priority: string value
        state: string value
        employee-id: string value
        company-id: string value
      }```
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-project/projects```
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-project/projects/{value of project id}```
    - [DELETE] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-project/projects/{value of project id}```
    - [PUT] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-project/projects/{value of project id}```
      ```payload {
        nale: string value
        description: string value
        priority: string value
        state: string value
        employee-id: string value
        company-id: string value
      }``
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-project/projects/employees/{value of employee id}```: projects assigned at given employee
    - [GET] ```http://192.168.49.2:31688/k8s-kafka-avro-aepc-bs-ms-project/projects/companies/{value of company id}```: projects assigned at given company

   for [POST] and [PUT], ***employee-id, company-id**, project ms sends request to eployee and company ms to ask employee and company related to employee-id and company-id. a resilience is managed if theses ms are down

