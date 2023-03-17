# Statistics with message oriented middleware

This repository contains an example that shows how to use JMS in a spring application. The application calculates the average, minimum and maximum of numbers which are provided via a web form.

## Projects

The following two projects provide the application, which communicates through JMS.
Both projects contain the application.properties, which define relevant configurations for the activemq connection.
The first JMS integration represented by the functionality to add a new data point uses the `JmsTemplate` class offered by the spring framework to send a message to a message queue.
The second JMS integration represented by the functionality to call a aggregation function uses a more native implementation of a synchronous JMS connection. 

### statistic-web
The spring web project. It provides the web view using the MVC pattern. 

### statistics-backend
The backend project. It contains the two message listeners, which consume the messages from the JMS queues.

## How to run the application
Start an ActiveMQ Artemis broker. This is, for instance, possible using the docker image provided at [quay.io](https://quay.io/artemiscloud/activemq-artemis-broker).
You can run this docker image by installing docker and then executing the following command on the command line:

    docker run -e AMQ_USER=admin -e AMQ_PASSWORD=admin -e AMQ_SECURITY_ENABLED=false -p8161:8161 -p61616:61616 --name artemis quay.io/artemiscloud/activemq-artemis-broker
    
Then start both projects by navigating into their root folder an then calling:

    ./mvnw spring-boot:run
    
The application is then available under [http://localhost:8080](http://localhost:8080).