package Server_side;

import helpers.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import Auth.Auth;
import Beans.Stock;

public class Main_Server {

	/**
	 * Process to spawn the thread that gets the prices and sends them to the
	 * communtication class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// get the program config
		final Configuration configuration = new Configuration();

		// set up the data structures and data

		ConcurrentLinkedQueue<Stock> concurrentLinkedQueue = new ConcurrentLinkedQueue<Stock>();
		HashSet<Stock> hashStockSet = null;

		try {
			hashStockSet = getHashSet();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// set up the threads for processing:

		Auth authServiceThread = new Auth(configuration);
		GetStockUpdates updateThread = new GetStockUpdates(
				concurrentLinkedQueue, hashStockSet, configuration);
		SendStockUpdates sendThread = new SendStockUpdates(
				concurrentLinkedQueue, configuration);


		// start the threads
		authServiceThread.start();
		updateThread.start();
		sendThread.start();

	}

	private static HashSet<Stock> getHashSet() throws IOException,
	ClassNotFoundException {

		ObjectInputStream in = null;

		try {
			FileInputStream fis = new FileInputStream("HashStockSet.data");
			in = new ObjectInputStream(fis);
			HashSet<Stock> temp = (HashSet<Stock>) in.readObject();
			return temp;
		}
		finally{
			if (null != in) {
				in.close();
			}
		}
	}
}
