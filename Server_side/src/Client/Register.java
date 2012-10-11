package Client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.SocketFactory;


/**
 * Simple class to handle the registering of a user, possibility of using SSL
 * for the connection.
 * 
 * 
 * @author lewismclean
 * 
 */
public class Register {

	public static void main (String args[]) throws IOException {

		// to trick google into giving me the img via url
		System.setProperty("http.agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

		// TODO read from prefs
		int port = 2000;

		SocketFactory socketFactory = SocketFactory.getDefault();
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
				String userInput;
				// send over the user
				userInput = stdIn.readLine();
				os.writeBytes(userInput);
				os.writeByte('\n');
				// open the url thats send back to set up the TOTP algo
				ImageDemo imgThread = new ImageDemo(new URL(is.readLine()));
				imgThread.start();

				// tidy up
				os.close();
				is.close();
				s.close();
			} catch (IOException e) {
				System.err.println("I/O failed on the connection to user creation service");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}


