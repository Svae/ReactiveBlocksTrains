package networktest.amqp2;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import no.ntnu.item.arctis.runtime.Block;

import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.proton.message.Message;
import org.apache.qpid.proton.messenger.Messenger;
import org.apache.qpid.proton.messenger.MessengerException;

import com.bitreactive.library.amqp.AmqpMessage;
import com.bitreactive.library.amqp.BrokerAddress;

public class AMQP2 extends Block {
	
	private Messenger messenger = null;
	private boolean isStopped = true;
	private HashMap<BrokerAddress, Thread> subs;
	
	public void start(final Set<BrokerAddress> addr) {
		isStopped = false;
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				String err = init();
				if (err != null) {
					sendToBlock("FAILED", err);
					isStopped = true;
					return;
				}
				if (addr != null && !addr.isEmpty()) {
					subscribe(addr);
				}
				logger.trace("Receiver ready");
				if (!isStopped) {
					sendToBlock("READY");
				}
				while (!isStopped) {
	                messenger.recv();
	                while (messenger.incoming() > 0) {
						logger.trace("Message available");
	                    Message msg = messenger.get();
	                    if (msg == null) {
							logger.trace("Failed to get message");
	                    	if (!isStopped) {
	                    		sendToBlock("ERROR", "Failed to get an incoming message!");
	                    	}
	                    } else {
	                    	AmqpMessage m = decode(msg);
							logger.trace("Received message decoded, content=" + m.body);
	                    	if (!isStopped) {
	                    		sendToBlock("RECEIVED", m);
	                    	}
	                    }
	                    if (isStopped) {
	                        break;
	                    }
	                }
				}
                messenger.stop();
                messenger = null;
		}
		};
		runAsync(r);
	}
	
	public void addSubscriptions(final Set<BrokerAddress> addr) {
		if (addr == null || addr.isEmpty()) {
			return;
		}
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				subscribe(addr);
			}
		};
//		subs.put(key, value)
		runAsync(r);
	}
	
	private void subscribe(Set<BrokerAddress> addr) {
		for (BrokerAddress a : addr) {
			try {
				messenger.subscribe(a.uri);
			} catch (MessengerException e) {
				if (!isStopped) {
					sendToBlock("ERROR", "Failed to subscribe to" + a.uri + "! " + e.getLocalizedMessage());
				}
			}
		}
	}
	
	private static AmqpMessage decode(Message msg) {
        AmqpMessage m = new AmqpMessage();
        m.toAddr = msg.getAddress();
        m.subject = msg.getSubject();
        if (msg.getBody() instanceof AmqpValue) {
        	Object val = ((AmqpValue) msg.getBody()).getValue(); 
        	if (val instanceof String) {
        		m.body = (String) val;
        	}
        }
        if (m.body == null) {
        	m.body = "Body is not a String!";
        }
        if (msg.getCreationTime() > 0l) {
        	m.creationTime = new Date(msg.getCreationTime());
        }
        m.msgId = msg.getMessageId();
        return m;
	}
	
	private String init(){
		try {
			messenger = Messenger.Factory.create();
			messenger.start();
			logger.trace("Messenger started!");
			return null;
		} catch (IOException e) {
			messenger = null;
			logger.trace("Failed to satart a messenger for sending!");
			return "Failed to start messenger!";
			// TODO: handle exception
		}
	}
	
	public void stop() {
		isStopped = true;
	}

}
