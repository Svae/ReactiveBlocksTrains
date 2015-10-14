package networktest.system.component;

import java.util.ArrayList;
import java.util.HashMap;

import util.Message;
import no.ntnu.item.arctis.runtime.Block;

public class Component extends Block {

	
	
	public HashMap<String,String> init(){
		HashMap<String, String> p = new HashMap<>();
		p.put("HOST", "192.168.0.100");
		p.put("EXCHANGE_NAME", "train_communication");
		return p;
	}


	public Message createMsg() {		
		return new Message("train", "test");
	}
	
	public void print(String s){
		System.out.println(s);
	}
	
	public void printMsg(Message m){
		System.out.println(m.getBody().getClass().toString());
		System.out.println(m.getBody().toString());
		System.out.println(m.getEnvelope().getClass());
		System.out.println("Time:" + m.getProperties().getTimestamp());
		
	}
	
	public void ready(){
		System.out.println("Ready");
	}


	public String topic() {
		return "train";
	}

}
