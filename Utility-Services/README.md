# utility services
This project contains utility tools:
## utility microservices
- configuration server: **microservices-config-service**
- registration server: **microservices-registry-service**
- a gateway service: **gateway service**

## kafka infrastructure
- zookeeper: to manage kafka brokers
- kafka-server(3 brokers) **kafka-broker-1**,**kafka-broker-2**,**kafka-broker-3**: to publish events into topics and disbribute events to consumers
- schema registry: to define and register the schema of the event
- avro schema:to serialize kafka events
- kafdrop: a kafka UI

## database
mysql- db to perist data

the utility tools are deployed into docker containers: 
    
   ```docker compose -f utility-services-compose.yml up -d```