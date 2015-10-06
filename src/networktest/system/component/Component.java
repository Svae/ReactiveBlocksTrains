package networktest.system.component;

import java.util.ArrayList;
import java.util.HashMap;

import util.Message;
import no.ntnu.item.arctis.runtime.Block;

public class Component extends Block {

	
	
	public HashMap<String,String> init(){
		HashMap<String, String> p = new HashMap<>();
		p.put("HOST", "192.168.0.100");
		p.put("EXCHANGE_NAME", "train_logs");
		return p;
	}


	public Message createMsg() {
		System.out.println("Ready, creating message");
		String[] s = new String[3];
		s[0] = "Hei";
		s[1] = "paa";
		s[2] = "deg!";

		return new Message("train", s);
	}
	
	public void print(String s){
		System.out.println(s);
	}
	
	public void printMsg(Message m){
		/*String[] s = (String[])m.getBody();
		for (int i = 0; i < 3; i++) {
			System.out.println(s[i]);
		}*/
		System.out.println(m.getBody().getClass().toString());
		ArrayList<String> l = (ArrayList<String>)m.getBody();
		
	}
	
	public void ready(){
		System.out.println("Ready");
	}


	public String topic() {
		return "*";
	}

}
