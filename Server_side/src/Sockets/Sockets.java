/**
 * 
 */
package Sockets;

import helpers.Configuration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.SocketFactory;

import Beans.Stock;
import Interfaces.SendServerSide;

/**
 * @author lewismclean
 *
 */
public class Sockets extends Thread implements SendServerSide {

	private final Configuration configuration;
	private final HashMap<String, Socket> clients;
	private final Socket auth;
	private final ConcurrentLinkedQueue<ServerSocket> newClients;
	private final HandleClients handleClientsThread;
	private boolean isDebugging = false;

	/**
	 * 
	 */
	public Sockets(Configuration configuration) {
		this.configuration = configuration;
		auth = setupAuthSocket();
		clients = new HashMap<String, Socket>();
		newClients = new ConcurrentLinkedQueue<ServerSocket>();
		handleClientsThread = new HandleClients(newClients, configuration);
		handleClientsThread.run();
		if (configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}
		if (isDebugging) {
			System.err.println(this.toString() + " Started");
		}
	}

	private Socket setupAuthSocket() {

		int port = Integer.parseInt(configuration
				.getProperty("SERVER_AUTH_SERVICE"));

		SocketFactory socketFactory = SocketFactory.getDefault();
		Socket s = null;
		try {
			s = socketFactory.createSocket("127.0.0.1", port);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return s;

	}


	/* (non-Javadoc)
	 * @see Interfaces.SendServerSide#sendTick(Beans.Stock)
	 */
	@Override
	public void sendTick(Stock tick) {

		for (ServerSocket clientConnection : newClients) {
			// get the token
			// check auth
			// add to map with timeout
		}
		// for each socket in map
		// check the time
		// check the connection
		// send the stock

	}

	/* (non-Javadoc)
	 * @see Interfaces.SendServerSide#getConnectedClients(java.util.List)
	 */
	@Override
	public void getConnectedClients(List clients) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see Interfaces.SendServerSide#getNumberOfConnectedClients()
	 */
	@Override
	public int getNumberOfConnectedClients() {
		// TODO Auto-generated method stub
		return 0;
	}

}
