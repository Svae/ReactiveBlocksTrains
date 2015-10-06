package networktest.client;


import com.bitreactive.library.amqp.AmqpMessage;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient.Parameter;

import no.ntnu.item.arctis.runtime.Block;

public class Client extends Block {

	private String host ="blocks2.bitreactive.com";
	private String subject = "Train_Network_Test";
	
	public Parameter initAMQP() {
		return new BrokeredAMQPClient.Parameter();
	}

	public AmqpMessage createMessage() {
		System.out.println("Sending message to: " + host + " Subject: " + subject);
		return new AmqpMessage(host, subject, "Test1");
	}
				
	
//	public Parameters initMQTTParam() {		
//		MQTTConfigParam m = new MQTTConfigParam("192.168.0.100");
//		m.addSubscribeTopic("IVProductionsTrainController");
//		Parameters p = new Parameters(m);
//		return p;
//	}
}
