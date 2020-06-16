package de.wwu.pi.statisticsweb.service;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private JmsTemplate jmstemplate;

	/**
	 * Sends a message to the statistics backend with a new DataPoint
	 * @param Double x new DataPoint.
	 */
	public void addStatistics(Double x) {
		jmstemplate.convertAndSend("StatisticsQueue", x);
	}

	/**
	 * Query the minimum of all stored DataPoints.
	 * @return the minimum.
	 */
	public Double getMinimum() {
		return this.callFunction("minimum");
	}

	/**
	 * Query the maximum of all stored DataPoints.
	 * @return the maximum.
	 */
	public Double getMaximum() {
		return this.callFunction("maximum");
	}

	/**
	 * Query the average of all stored DataPoints.
	 * @return the average.
	 */
	public Double getAverage() {
		return this.callFunction("average");
	}

	/**
	 * Retrieves a function value from the backend using synchronous JMS messages.
	 * @param String function to be called. 
	 * @return value of this function for all DataPoints
	 */
	protected Double callFunction(String function) {

		Session session;
		Double result = 0.0;
		try {
			// Create connection and Session.
			Connection con = jmstemplate.getConnectionFactory().createConnection();
			session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Create temporary queue for synchronous responses.
			Queue temporaryqueue = session.createTemporaryQueue();
			
			// Create message.
			TextMessage textMessage = session.createTextMessage(function);
			textMessage.setJMSReplyTo(temporaryqueue);
			textMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			// Start connection.
			con.start();

			// Send message using a message producer.
			MessageProducer producer = session.createProducer(new ActiveMQQueue("FunctionsQueue"));
			producer.send(textMessage);
			// Wait for response to arrive.
			MessageConsumer consumer = session.createConsumer(temporaryqueue);
			ActiveMQObjectMessage message = (ActiveMQObjectMessage) consumer.receive();

			// Retrieve answer and close connection.
			result = (Double) message.getObject();
			con.close();

		} catch (JMSException e) {
			e.printStackTrace();
		}
		return result;

	}
}
