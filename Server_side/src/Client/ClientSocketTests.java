package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

public class ClientSocketTests {

	public ClientSocketTests() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException,
	IOException {
		// TODO Auto-generated method stub

		int serverPort = 3000;
		PrintWriter out = null;
		BufferedReader in = null;


		// Change this to create an SSLSocketFactory instead of a SocketFactory.
		SocketFactory socketFactory = SocketFactory.getDefault();

		// We do not need to change anything else.
		// That's the beauty of using factories!
		Socket s = socketFactory.createSocket("127.0.0.1", 3000);

		DataInputStream console = new DataInputStream(s.getInputStream());
		DataOutputStream streamOut = new DataOutputStream(s.getOutputStream());

		// Scanner scan = new Scanner(System.in);
		// String usrInput;

		// System.out.print("Enter the input");
		// usrInput = scan.next();

		streamOut.writeBytes("lewis");
		streamOut.writeByte('\n');
		System.out.println("echo: " + console.readUTF());


		out.close();
		s.close();

	}

}

