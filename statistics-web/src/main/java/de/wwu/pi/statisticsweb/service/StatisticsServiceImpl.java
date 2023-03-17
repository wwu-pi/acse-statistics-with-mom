package de.wwu.pi.statisticsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import jakarta.jms.JMSException;
import jakarta.jms.ObjectMessage;

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

		Double result = 0.0;
		try {		
			// Define message creator.
			MessageCreator creator = (session) -> (session.createTextMessage(function));

			// Send and receive a message. A temporary queue is automatically added as replyTo address.
			ObjectMessage message = (ObjectMessage) jmstemplate.sendAndReceive("FunctionsQueue", creator);

			// Retrieve the result.
			result = (Double) message.getObject();

		} catch (JMSException e) {
			e.printStackTrace();
		}
		return result;

	}
}
