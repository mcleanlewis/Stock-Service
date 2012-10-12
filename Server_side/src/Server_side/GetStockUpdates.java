package Server_side;

import helpers.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import Beans.Stock;

/**
 * producer class for the producer-consumer thread model. Uses a non-blocking
 * queue.
 * 
 * @author lewismclean
 */
public class GetStockUpdates extends Thread {

	private final ConcurrentLinkedQueue<Stock> sharedQueue;
	private final HashSet<Stock> stock;
	private final Configuration configuration;
	private boolean isDebugging = false;

	public GetStockUpdates(ConcurrentLinkedQueue<Stock> emptyQueue,
			HashSet<Stock> stock, Configuration configuration) {

		sharedQueue = emptyQueue;
		this.stock = stock;
		this.configuration = configuration;
		if (configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}

	}

	@Override
	public void run() {

		if (isDebugging) {
			System.err.println(this.toString() + "get stock Started");
		}

		// go until interupt
		while (true) {

			for (Stock code : stock) {
				try {
					getStockInfo(code);
					// Thread.sleep(1000);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// yahoo playing up
					e.printStackTrace();
				}
			}
		}
	}

	public void getStockInfo(Stock current) throws Exception {

		URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s="
				+ current.getName() + "&f=st1jkw4l1lb2b3&e=.csv");
		URLConnection yc = yahoofin.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String line = in.readLine();
		if (current.process(line)) {
			// send to the consumer for processing
			if (isDebugging) {
				// System.out.println(line);
			}
			sharedQueue.add(current);
		}

		else if (isDebugging) {
			sharedQueue.add(current);
			System.out.println("No update");
		}
	}
}


