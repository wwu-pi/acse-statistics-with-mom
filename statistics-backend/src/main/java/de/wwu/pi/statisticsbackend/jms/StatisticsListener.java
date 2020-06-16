package de.wwu.pi.statisticsbackend.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import de.wwu.pi.statisticsbackend.data.model.DataPoint;
import de.wwu.pi.statisticsbackend.data.repo.DataPointRepository;

@Component
public class StatisticsListener {

	@Autowired
	private DataPointRepository repo;

	@JmsListener(destination = "StatisticsQueue")
	/**
	 * Offers the functionality to add new data points.
	 * 
	 * @param x value of the new data point
	 */
	public void addDataPoint(Double x) {
		DataPoint dp = new DataPoint(x);
		repo.save(dp);
	}

	@JmsListener(destination = "FunctionsQueue")
	/**
	 * Offers the functionality to query one of three functions. The function is
	 * called for all stored data points and the result is send back via JMS to the
	 * reply destination.
	 * 
	 * @param message
	 * @param session
	 * @throws JMSException
	 */
	public void callFunction(Message message, Session session) throws JMSException {
		// Retrieve called function from message object.
		String function = (String) ((ActiveMQTextMessage) message).getText();

		// Create new object message for the response.
		final ObjectMessage responseMessage = session.createObjectMessage();

		// Query the result and add it to the message.
		switch (function) {
		case "average":
			Double avg = repo.findAvgX();
			responseMessage.setObject(avg);
			break;
		case "minimum":
			Double min = repo.findMinX();
			responseMessage.setObject(min);
			break;
		case "maximum":
			Double max = repo.findMaxX();
			responseMessage.setObject(max);
			break;
		}

		// Send the message back to the reply destination.
		final MessageProducer producer = session.createProducer(message.getJMSReplyTo());
		producer.send(responseMessage);
	}

}
