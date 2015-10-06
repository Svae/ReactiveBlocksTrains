package networktest.receiver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import util.Message;
import util.Tuple;
import no.ntnu.item.arctis.runtime.Block;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Receiver extends Block {

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Channel channel;
	private String queue_name;
	private String exchange_name;
	private Consumer consumer;
	private String defaultExchange = "train_logs";

	public void init(HashMap<String,String> properties){
		
		if(factory == null){
			exchange_name = properties.containsKey("EXCHANGE_NAME") ? properties.get("EXCHANGE_NAME") : defaultExchange;
			factory = new ConnectionFactory();
			factory.setHost(properties.containsKey("HOST") ? properties.get("HOST") : "192.168.0.100");
			try {
				connection = factory.newConnection();
				channel = connection.createChannel();
				channel.exchangeDeclare(exchange_name, "topic");
				queue_name = channel.queueDeclare().getQueue();
			} catch (IOException | TimeoutException e1) {
				sendToBlock("FAILED", e1);
			} 
		}
		
		consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope,
		                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
		        Message message = new Message(properties, envelope);
		        Object value = decodeBody(body);
		        if(value == null){ 
		        	sendToBlock("ERROR", "Failed to deserialize message body");
		        	return;
		        }
		        message.setBody(value);
		        sendToBlock("RECEIVED", message);
		      }
		    };
		try {
			channel.basicConsume(queue_name, true, consumer);
		} catch (IOException e) {
			sendToBlock("FAILED", e);
		}
		sendToBlock("READY", new Tuple<String, Connection>(exchange_name, connection));

	}
	
	private Object decodeBody(byte[] body){
		Gson gson = new Gson();
		try {
			return gson.fromJson(new String(body, "UTF-8"), Object.class);
		} catch (JsonSyntaxException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	public void addTopic(String topic) {
		try {
			channel.queueBind(queue_name, exchange_name, topic);
		} catch (IOException e) {
			sendToBlock("ERROR", e);
		}
	}

	public void addTopics(List<String>topics) {
		for(String t : topics) addTopic(t); 
	}

	public void removeTopic(String topic) {
		try {
			channel.queueUnbind(queue_name, exchange_name, topic);
		} catch (IOException e) {
			sendToBlock("ERROR", e);
		}
	}

	public void removeTopics(List<String> topics) {
		for(String t : topics) removeTopic(t);
	}
	
	public void stop(){
		try {
			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			sendToBlock("ERROR", e);
		}
	}

	// Do not edit this constructor.
	public Receiver() {
	}

}
