package redis_consumer;

import java.util.List;

import redis.clients.jedis.Jedis;

public class Main {

	public static void main(String[] args) {
		Jedis redis = new Jedis("localhost");
		List<String> messages = null;
		while (true) {
			System.out.println("------ Waiting for a message in the queue");
			messages = redis.blpop(0, "queue");
			System.out.println(" VALUE:" + messages.get(1));
			if (messages.get(1).equalsIgnoreCase("exit")) {
				break;
			}
		}
		redis.close();
	}

}
