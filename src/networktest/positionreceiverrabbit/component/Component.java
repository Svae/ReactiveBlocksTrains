package networktest.positionreceiverrabbit.component;

import java.util.HashMap;


import no.ntnu.item.arctis.runtime.Block;
import util.Message;

public class Component extends Block {


	public HashMap<String, String> createParameter() {
		logger.info("About to start AMQP Client...");
		return new HashMap<String,String>();
	}

	public void received(Message m) {
		logger.info("Message received, content=" + m.getBody().toString());
	}


	public void error(String e) {
		logger.error(e);
	}

	public String ready() {
		logger.info("Ready!");
		return "*";
	}
}
