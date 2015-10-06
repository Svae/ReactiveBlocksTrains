package util;

import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Envelope;

public class Message {
	
	private String topic, className;
	private Object body;
	private BasicProperties properties;
	private Envelope envelope;
	
	public Message(String topic, Object body) {
		this.body = body;
		this.topic = topic;
	}

	public Message(BasicProperties properties, Envelope envelope){
		this.properties = properties;
		this.envelope = envelope;
	}
	
	public Envelope getEnvelope(){
		return envelope;
	}
	
	public BasicProperties getProperties(){
		return properties;
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
