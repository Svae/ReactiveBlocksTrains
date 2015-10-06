package networktest.server;

import com.bitreactive.library.amqp.AmqpMessage;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient.Parameter;

import no.ntnu.item.arctis.runtime.Block;

public class Server extends Block {

	private String host ="blocks2.bitreactive.com";
	private String subject = "Train_Network_Test";
	
	public Parameter initAMQP() {
		return new BrokeredAMQPClient.Parameter();
	}

	public AmqpMessage createMessage() {
		System.out.println("Sending message to: " + host + " Subject: " + subject);
		return new AmqpMessage(host, subject, "Test1");
	}
				
	public void received(AmqpMessage m) {
		System.out.println("Message received, createdAt=" + m.creationTime.toString() + ", subject=" + m.subject + ", content=" + m.body);
	}
}

