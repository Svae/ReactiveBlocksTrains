package networktest.positionpublisherrabbit.component;

import java.util.HashMap;

import com.bitreactive.examples.amqp.gpssensorsimulator.Coordinate;

import no.ntnu.item.arctis.runtime.Block;
import util.Message;

public class Component extends Block {


	public HashMap<String, String> createParameter() {
		logger.info("About to start AMQP Client...");
		HashMap<String,String> p = new HashMap<String,String>();
		return p;
	}

	public Message createMsg(Coordinate content) {
		
		return new Message("train", content);
	}

	public void published() {
		logger.info("Message published");
	}

	public void sensorErr() {
		logger.error("Error initializing GPS Sensor Simulator!");
	}

	public void amqpErr(String e) {
		logger.error(e);
	}

	public void error(String e) {
		logger.error(e);
	}
}
