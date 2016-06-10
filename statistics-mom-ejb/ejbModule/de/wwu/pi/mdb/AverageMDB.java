package de.wwu.pi.mdb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;

/**
 * Message-Driven Bean implementation class for: AverageMDB
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/StatisticsTopic") })
public class AverageMDB implements MessageListener {

	@Resource(lookup = "java:/JmsXA")
	private ConnectionFactory cf;

	public AverageMDB() {
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		Connection conn = null;
		Session session = null;

		try {
			System.out.println("Average-Server: Message received");
			MapMessage msg = (MapMessage) message;
			int anz = msg.getInt("count");
						
			double result = 0.0;
			for (int i = 0; i < anz; i++)
				result += msg.getDouble("arg" + i);
			result /= anz;

			conn = cf.createConnection();
			conn.start();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination replyTo = message.getJMSReplyTo();
			MessageProducer producer = session.createProducer(replyTo);
			StreamMessage reply = session.createStreamMessage();
			reply.setStringProperty("ResultType", "Average");
			reply.writeDouble(result);

			producer.send(reply);
			producer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
