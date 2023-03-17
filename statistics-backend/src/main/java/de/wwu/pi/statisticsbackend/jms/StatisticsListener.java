package de.wwu.pi.statisticsbackend.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import de.wwu.pi.statisticsbackend.data.model.DataPoint;
import de.wwu.pi.statisticsbackend.data.repo.DataPointRepository;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;

@Component
public class StatisticsListener {

	@Autowired
	private JmsTemplate jmstemplate;
	
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
	public void callFunction(Message message) throws JMSException {
		// Retrieve called function from message object.
		String function = (String) ((TextMessage) message).getText();

		// Create new message creator for the response.
		MessageCreator creator = (session) -> {
			// Create a object message to encapsulate the response value.
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
			return responseMessage;
		};

		// Send the result to the temporary reply queue.
		jmstemplate.send(message.getJMSReplyTo(), creator);
	}

}
