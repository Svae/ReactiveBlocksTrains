package networktest.send;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import no.ntnu.item.arctis.runtime.Block;
import util.Message;
import util.Tuple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send extends Block {

	private Connection connection = null;
	private Channel channel = null;
	private String exchange;
	private String topic;
	
	public void send(final String m){
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try {
					channel.basicPublish(exchange,topic, null, m.getBytes());
					System.out.println(m);
					sendToBlock("SENT");
				} catch (IOException e) {
					sendToBlock("ERROR", e);
				}
				
			}
		};
		runAsync(r);
	}
	
	public void init(Tuple<String, Connection> parameters){

		if(parameters != null){
			exchange = parameters.getValue1();
			this.connection = parameters.getValue2();
			try {
				channel = connection.createChannel();
				channel.exchangeDeclare(exchange, "topic");
			} catch (IOException e) {
				sendToBlock("ERROR", e);
			}
		}
	}

	public void stop() {
		try {
			channel.close();
			connection.close();

		} catch (IOException | TimeoutException e) {
			sendToBlock("ERROR", e);
		} 
	}

	public void setExchange(Message m) {
		this.topic = m.getTopic();
	}

	public Object getMessage(Message m) {
		return m.getBody();
	}
}
