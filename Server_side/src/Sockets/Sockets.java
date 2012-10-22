/**
 * 
 */
package Sockets;

import helpers.Configuration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import Beans.SocketWrapper;
import Beans.Stock;
import Interfaces.SendServerSide;

/**
 * @author lewismclean
 *
 */
public class Sockets extends Thread implements SendServerSide {

	private final Configuration configuration;
	private final ConcurrentLinkedQueue<SocketWrapper>	newClients;
	private final HandleClients handleClientsThread;
	private boolean isDebugging = false;

	/**
	 * 
	 */
	public Sockets(Configuration configuration) {
		this.configuration = configuration;
		newClients = new ConcurrentLinkedQueue<SocketWrapper>();
		handleClientsThread = new HandleClients(newClients, configuration);
		handleClientsThread.start();
		if (configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}
		if (isDebugging) {
			System.err.println(this.toString() + "sockets Started");
		}
	}




	/* (non-Javadoc)
	 * @see Interfaces.SendServerSide#sendTick(Beans.Stock)
	 */
	@Override
	public void sendTick(Stock tick) {

		//System.out.print("send called  ");

		for (SocketWrapper clientConnection : newClients) {

			//System.out.println("picked up socket");
			DataOutputStream os = null;
			DataInputStream is = null;

			Socket socket = clientConnection.getSocket();
			// System.out.println("got client socket ");

			if (socket.isConnected()) {

				DataInputStream stdIn = new DataInputStream(System.in);

				try {
					os = new DataOutputStream(socket.getOutputStream());
					is = new DataInputStream(socket.getInputStream());
				} catch (UnknownHostException e) {
					System.err.println("Don't know about host");
				} catch (IOException e) {
					System.err.println("Couldn't get I/O for the connection token service");
				}
				long timeleft = clientConnection.getTokenExpiration() - System.currentTimeMillis();
				System.out.println("TIME LEFT socket " + timeleft);

				if (timeleft > 0) {

					if ((socket != null) && (os != null) && (is != null)) {
						try {
							System.err.println("sent ****");
							os.writeBytes(tick.toString());
							os.writeByte('\n');
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else {
					try {
						os.writeBytes("EXPIRED_TOKEN");
						os.writeByte('\n');
						String response = is.readLine();
						if (response != null) {
							clientConnection.setToken(response);
						} else {
							throw new IOException("no response from client");
						}

					} catch (IOException e) {
						System.err.println("client disconnected");
						clientConnection.setToken("");
						clientConnection.setTokenExpiration(System.currentTimeMillis());
						// clientConnection.setSocket(null);
						// newClients.remove(clientConnection);
					}
				}
			}

		}
		tidyUpQueue();

	}

	private void tidyUpQueue() {
		for (SocketWrapper socketWrapper : newClients) {
			if (socketWrapper.getToken().equals("")) {
				newClients.remove(socketWrapper);
			}
		}
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
