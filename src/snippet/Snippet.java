package snippet;

public class Snippet {
	ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("blocks2.bitreactive.com");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false,false,false, null);
			String message = "Hello world!";
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
			channel.close();
			connection.close();
}

