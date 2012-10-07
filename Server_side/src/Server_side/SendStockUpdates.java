package Server_side;

import helpers.Configuration;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import Beans.Stock;

public class SendStockUpdates extends Thread {

	private final ConcurrentLinkedQueue<Stock> sharedQueue;
	private final Configuration configuration;
	private boolean isDebugging = false;
	private final int dataAge;
	private Socket socket;

	// private graeme imp


	public SendStockUpdates(ConcurrentLinkedQueue<Stock> emptyQueue,
			Configuration configuration) {
		sharedQueue = emptyQueue;
		this.configuration = configuration;
		if (configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}
		dataAge = Integer.parseInt(configuration.getProperty("DATA_AGE"));
	}

	@Override
	public void run() {

		if (isDebugging) {
			System.err.println("sendStock Started");
		}

		while (true) {

			if (!sharedQueue.isEmpty()) {
				Stock peeked = sharedQueue.peek();
				if ((System.currentTimeMillis() - peeked.getUpdateTime()) == dataAge) {
					sharedQueue.poll();
					// been too long, old data
				} else {
					// send to interface

				}
			}
		}
	}
}
