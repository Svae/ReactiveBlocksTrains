package networktest.performancetest.component;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.bitreactive.library.amqp.AmqpError;
import com.bitreactive.library.amqp.AmqpMessage;
import com.bitreactive.library.amqp.AmqpMessage.SendProperty;
import com.bitreactive.library.amqp.BrokerAddress;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient.Parameter;

import no.ntnu.item.arctis.runtime.Block;

public class Component extends Block {

	private String host ="blocks2.bitreactive.com";
	private String subject = "Train_Network_Test";
	private String queue = "myTrain";
	
	private long tSent;
	public Parameter initParams() {
		Set<BrokerAddress> addr = new HashSet<>();
		addr.add(new BrokerAddress(host, queue));
		return new BrokeredAMQPClient.Parameter(addr);
	}
	
	public AmqpMessage createMessage(){
		System.out.println("Sending message at " + System.currentTimeMillis());
		AmqpMessage m =new  AmqpMessage(host, 5672, queue, subject, new Timestamp(System.currentTimeMillis()).toString());
		m.sendProperty = new SendProperty(2000);
		return m;
	}
	
	public void recv(AmqpMessage m){
		System.out.println("Message received, createdAt=" + m.creationTime.toString() + ", subject=" + m.subject + ", content=" + m.body);
	}

	public void amqpErr(AmqpError e) {
		if (e.message != null) {
			logger.error(e.errorMessage + " for AMQP message, content= " + e.message.body);
		} else {
			logger.error(e.errorMessage);
		}
	}
}
