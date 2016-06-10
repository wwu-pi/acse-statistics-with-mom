package de.wwu.pi.web.beans;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.Topic;

@ManagedBean
@RequestScoped
public class Statistics {

	double x, y, z, median, average;

	@Resource(lookup = "java:/JmsXA")
	private ConnectionFactory cf;

	@Resource(lookup = "java:/topic/StatisticsTopic")
	private Topic topic;

	public Statistics() {

	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}
	
	public double getMedian() {
		return median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public void send() {
		try {
			Connection connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session.createProducer(topic);
			Queue temporaryQueue = session.createTemporaryQueue();
			MapMessage message = session.createMapMessage();
			
			List<Double> numbers = new LinkedList<Double>();
			numbers.add(x);
			numbers.add(y);
			numbers.add(z);
			int size = numbers.size();		
			
			message.setInt("count", size);

			for (int i = 0; i < size; i++)
				message.setDouble("arg" + i, numbers.get(i));

			message.setJMSReplyTo(temporaryQueue);
			sender.send(message);

			connection.start();
			MessageConsumer consumer = session.createConsumer(temporaryQueue, "ResultType='Average'");
			StreamMessage resultmsg = (StreamMessage) consumer.receive(0);
			if (resultmsg == null)
				throw new Exception("Client: No average value received...");
			average = resultmsg.readDouble();

			consumer = session.createConsumer(temporaryQueue, "ResultType='Median'");
			resultmsg = (StreamMessage) consumer.receive(0);
			if (resultmsg == null)
				throw new Exception("Client: No median value received...");
			median = resultmsg.readDouble();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
