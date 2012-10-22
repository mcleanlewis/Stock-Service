package Client;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

public class ClientCli {

	/**
	 * @param args
	 */
	static SocketFactory	socketFactory	= SocketFactory.getDefault();
	static String	     token;
	static long	         tokenExpiration;
	private static String	userName;

	public static void main(String[] args) {

		String input = "";
		InputStreamReader inp = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(inp);

		while (!input.equals("exit")) {
			try {
				printCommands();
				input = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			switch (input.hashCode()) {
			case 112788:// reg
				try {
					register();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 115024:// tok
				try {
					getToken();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 107332:// log
				try {
					login();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 3127582:// exit
				System.out.println("Bye.");
				System.exit(0);
				break;

			default:
				printCommands();
				break;
			}
		}
	}

	private static void login() throws IOException, InterruptedException {

		// SimpleTableDemo gui = new SimpleTableDemo();

		// try {
		// gui.createAndShowGUI();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		// set up connection
		DatagramSocket socket = new DatagramSocket(8999);
		byte[] buf = new byte[256];
		InetAddress address = InetAddress.getByName("127.0.0.1");
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 9000);
		try {
			socket.send(packet);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("packet sent");

		packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			socket.close();
			socket = null;
		}
		String received = new String(packet.getData(), 0, packet.getLength());
		System.out.println(received);

		int port = Integer.parseInt(received);
		Thread.sleep(500);
		Socket s = socketFactory.createSocket("127.0.0.1", port);
		// localhost just now
		DataOutputStream os = null;
		DataInputStream is = null;
		DataInputStream stdIn = new DataInputStream(System.in);

		try {
			os = new DataOutputStream(s.getOutputStream());
			is = new DataInputStream(s.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection token service");
		}

		if ((s != null) && (os != null) && (is != null)) {

			os.writeBytes(new String(ClientCli.token));
			os.writeByte('\n');
			String response = "";
			while (true) {// gui.isVisible()) {

				while ((tokenExpiration > System.currentTimeMillis())
				        && !response.equals("EXPIRED_TOKEN")
				        && !response.equals("ERROR: bad token")) {

					// get prices and update
					response = is.readLine();
					if (response != null) {
						System.out.println("client - " + response);
						// gui.updatePrice(response);
					}
				}

				// renew token...
				renewToken();
				os.writeBytes(new String(token));
				os.writeByte('\n');
				response = is.readLine();

			}

			// test

		}
	}

	private static void renewToken() throws UnknownHostException, IOException {
		// TODO read from prefs
		int port = 2001;

		Socket s = socketFactory.createSocket("127.0.0.1", port);
		// localhost just now
		DataOutputStream os = null;
		DataInputStream is = null;
		DataInputStream stdIn = new DataInputStream(System.in);

		try {
			os = new DataOutputStream(s.getOutputStream());
			is = new DataInputStream(s.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection token service");
		}

		if ((s != null) && (os != null) && (is != null)) {
			try {
				os.writeBytes(userName.replace("><", "") + "><" + new String(token));
				os.writeByte('\n');

				String response = is.readLine();
				if ((response != null) && !response.equals("ERROR:Bad credentials")) {
					ClientCli.token = response;
					System.out.println("Received token, will expire in ~2 minutes");
					// TODO set the timeout function
					tokenExpiration = System.currentTimeMillis() + 28000;
					System.out.println(ClientCli.token);
				} else {
					System.err.println("ERROR:Bad credentials");
				}

			} catch (IOException e) {
				System.err.println("I/O failed on the connection to the token service");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				os.close();
				is.close();
				s.close();
			}
		}

	}

	private static void getToken() throws UnknownHostException, IOException {
		// TODO read from prefs
		int port = 2001;

		Socket s = socketFactory.createSocket("127.0.0.1", port);
		// localhost just now
		DataOutputStream os = null;
		DataInputStream is = null;
		DataInputStream stdIn = new DataInputStream(System.in);

		try {
			os = new DataOutputStream(s.getOutputStream());
			is = new DataInputStream(s.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection token service");
		}

		if ((s != null) && (os != null) && (is != null)) {
			try {
				System.out.println("enter user name");
				String userName;
				// send over the user
				userName = stdIn.readLine();
				ClientCli.userName = userName;
				System.out.println("enter password");
				char[] password;
				// send over the user
				password = stdIn.readLine().toCharArray();
				System.out.println("enter token");
				char[] token;
				// send over the user
				token = stdIn.readLine().toCharArray();
				os.writeBytes(userName.replace("><", "") + "><"
						+ new String(password).replace("><", "") + "><"
						+ new String(token).replace("><", ""));
				os.writeByte('\n');

				String response = is.readLine();
				if ((response != null) && !response.equals("ERROR:Bad credentials")) {
					ClientCli.token = response;
					ClientCli.userName = userName;
					System.out.println("Received token, will expire in ~2 minutes");
					tokenExpiration = System.currentTimeMillis() + 1000000;// just
					// under
					// 2
					// minutes
					System.out.println(ClientCli.token);
				} else {
					System.err.println("ERROR:Bad credentials");
				}

			} catch (IOException e) {
				System.err.println("I/O failed on the connection to the token service");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				os.close();
				is.close();
				s.close();
			}
		}
	}

	private static void register() throws UnknownHostException, IOException {
		// to trick google into giving me the img via url
		System.setProperty("http.agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

		// TODO read from prefs
		int port = 2000;

		Socket s = socketFactory.createSocket("127.0.0.1", port);
		// localhost just now
		DataOutputStream os = null;
		DataInputStream is = null;
		DataInputStream stdIn = new DataInputStream(System.in);

		try {
			os = new DataOutputStream(s.getOutputStream());
			is = new DataInputStream(s.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection");
		}

		if ((s != null) && (os != null) && (is != null)) {
			try {
				System.out.println("enter user name");
				String userName;
				// send over the user
				userName = stdIn.readLine();
				System.out.println("enter password");
				char[] password;
				// send over the user
				password = stdIn.readLine().toCharArray();
				os.writeBytes(userName.replace("><", "") + "><"
						+ new String(password).replace("><", ""));
				os.writeByte('\n');
				// open the url thats send back to set up the TOTP algo
				String response = is.readLine();
				if (!response.equals("ERROR:Username exists already")) {

					ImageDemo imgThread = new ImageDemo(new URL(response));
					imgThread.start();
					// tidy up
				} else {
					System.err.println("ERROR:Username exists already");
				}
				os.close();
				is.close();
				s.close();
			} catch (IOException e) {
				System.err.println("I/O failed on the connection to: taranis");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void printCommands() {
		System.out.println("reg(ister), (get )tok(en), log(in), exit");
	}
}
