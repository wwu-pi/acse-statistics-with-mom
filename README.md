# Statistics with message oriented middleware

This repository contains an example that shows how to use JMS in an Java EE web application. The application calculates the average and median for three numbers which are provided via a web form.

## Projects

### statistic-mom
The enterprise application project.

### statistics-mom-ejb
The EJB project. It contains the two message driven beans AverageMDB.java and Median.java, which consume the messages from the JMS topic.

The file META-INF/hornetq-jms.xml contains the configuration for the StatisticsTopic, which is used to exchange messages.

### statistics-mom-web
The dynamic web project. It contains the xhtml page and the Statistics.java backing bean. The backing bean sends a message that contains the entered numbers to the StatisticsTopics and opens a temporary queue that is used by the MDBs to send their responses.

## How to run the application
Configure your wildfly server in such a way that it runs HornetQ. Then deploy the enterprise application project on the server and access the application at localhost:8080/statistics-mom-web/.
