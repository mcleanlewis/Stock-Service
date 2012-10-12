package Sockets;

import helpers.Configuration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import Beans.SocketWrapper;

public class HandleClients extends Thread {

	private Configuration	                           configuration	= null;
	private final ConcurrentLinkedQueue<SocketWrapper>	newConnectedClients;
	private final Socket	                           authSocket;
	private boolean isDebugging = false;

	public HandleClients(ConcurrentLinkedQueue<SocketWrapper> emptyQueue,
			Configuration configuration) {
		this.configuration = configuration;
		newConnectedClients = emptyQueue;
		if (configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}
		authSocket = setupAuthSocket();

	}

	private Socket setupAuthSocket() {

		int port = Integer.parseInt(configuration.getProperty("SERVER_AUTH_SERVICE"));

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

	@Override
	public void run() {

		if (isDebugging) {
			System.err.println("handle clients started");
		}

		int udpPort = Integer.parseInt(configuration
				.getProperty("CLIENT_REQUEST_PORT"));

		DatagramSocket s = null;
		try {
			s = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buf = new byte[1000];
		int lastPort = Integer.parseInt(configuration.getProperty("PORT_LOW"));
		ServerSocketFactory serverSocketFactory = ServerSocketFactory
				.getDefault();



		while (true) {
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			try {
				s.receive(dp);
				if (isDebugging) {
					System.err
					.println("handle clients Received connection request");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ServerSocket newClientSocket = null;
			String user =  new String(dp.getData());
			buf = Integer.toString(lastPort).getBytes();

			Socket s1 = null;
			DatagramPacket out = new DatagramPacket(buf, buf.length, dp.getAddress(), udpPort - 1);
			try {
				newClientSocket = serverSocketFactory
						.createServerSocket(lastPort);
				newClientSocket.setSoTimeout(10000);
				s.send(out);

				try {
					// System.out.println(System.currentTimeMillis());
					s1 = newClientSocket.accept();
					// System.out.println(System.currentTimeMillis());
				} catch (SocketTimeoutException e) {
					System.out.println("connection timed out");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ((s1 != null)) {
				System.out.println("connection started");
				// auth token

				DataOutputStream os = null;
				DataInputStream is = null;

				try {
					os = new DataOutputStream(s1.getOutputStream());
					is = new DataInputStream(s1.getInputStream());
				} catch (UnknownHostException e) {
					System.err.println("Don't know about host");
				} catch (IOException e) {
					System.err.println("Couldn't get I/O for the connection token service");
				}
				String response = null;
				try {
					response = is.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ((response != null) && checkToken(response)) {
					System.out.println("connection token authenticated");
					newConnectedClients.add(new SocketWrapper(s1, response));
				} else {
					errorAndClose(s1);
					s1 = null;
				}
			}
			if (lastPort > Integer.parseInt(configuration
					.getProperty("PORT_HIGH"))) {
				lastPort = Integer.parseInt(configuration
						.getProperty("PORT_LOW"));
			} else {
				lastPort++;
			}
		}
	}

	private void errorAndClose(Socket s1) {

		DataOutputStream os = null;
		DataInputStream is = null;
		DataInputStream stdIn = new DataInputStream(System.in);

		try {
			os = new DataOutputStream(s1.getOutputStream());
			is = new DataInputStream(s1.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection token service");
		}

		if ((s1 != null) && (os != null) && (is != null)) {
			String response = "false";
			try {
				os.writeBytes("ERROR: bad token");
				os.writeByte('\n');
				response = is.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					s1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}



	public boolean checkToken(String request) {

		DataOutputStream os = null;
		DataInputStream is = null;
		DataInputStream stdIn = new DataInputStream(System.in);

		try {
			os = new DataOutputStream(authSocket.getOutputStream());
			is = new DataInputStream(authSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection token service");
		}

		if ((authSocket != null) && (os != null) && (is != null)) {
			String response = "false";
			try {
				os.writeBytes(request);
				os.writeByte('\n');
				// System.err.println("server to auth " + request);
				response = is.readLine();
				// System.err.println("back from auth " + response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Boolean.parseBoolean(response);
		}
		return false;

	}
}
