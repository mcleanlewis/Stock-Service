package Server_side;

import helpers.Configuration;

import java.util.concurrent.ConcurrentLinkedQueue;

import Beans.Stock;
import GT.GT_impl;
import Sockets.Sockets;

public class SendStockUpdates extends Thread {

	private final ConcurrentLinkedQueue<Stock> sharedQueue;
	private final Configuration configuration;
	private boolean isDebugging = false;
	private final int dataAge;
	private GT_impl gt_impl;
	private Sockets sockets;

	public SendStockUpdates(ConcurrentLinkedQueue<Stock> emptyQueue,
			Configuration configuration) {
		sharedQueue = emptyQueue;
		this.configuration = configuration;
		if (configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}
		dataAge = Integer.parseInt(configuration.getProperty("DATA_AGE"));

		if (configuration.getProperty("IMP").equals("GT")) {
			gt_impl = new GT_impl(this.configuration);
			sockets = null;
		} else {
			sockets = new Sockets(this.configuration);
			gt_impl = null;
		}
	}

	@Override
	public void run() {

		if (isDebugging) {
			System.err.println(this.toString() + " Started");
		}

		while (true) {

			if (!sharedQueue.isEmpty()) {
				Stock peeked = sharedQueue.peek();
				if ((System.currentTimeMillis() - peeked.getUpdateTime()) == dataAge) {
					sharedQueue.poll();
					// been too long, old data
				} else {
					// send to interface
					if ((null != gt_impl) && (null == sockets)) {
						gt_impl.sendTick(sharedQueue.poll());
					} else if ((null == gt_impl) && (null != sockets)) {
						sockets.sendTick(sharedQueue.poll());
					}
				}
			}
		}
	}
}
