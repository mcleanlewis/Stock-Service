package Auth;

import helpers.Configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class ServerService extends Service {

	private final int port;

	public ServerService(Configuration configuration, Thread parent)
			throws IOException {
		super(configuration, parent);

		port = Integer.parseInt(configuration
				.getProperty("SERVER_AUTH_SERVICE"));


	}

	@Override
	public void run() {

		if (super.isDebugging()) {
			System.err.println("serverAuthService Started");
		}
		while (true) {
			try {
				listenForRequest();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void listenForRequest() throws IOException {

		ServerSocketFactory serverSocketFactory = ServerSocketFactory
				.getDefault();

		ServerSocket serverSocket = serverSocketFactory
				.createServerSocket(port);

		Socket s = serverSocket.accept();
		if (super.isDebugging()) {
			System.err.println("serverAuthService Received connection");
		}
		BufferedInputStream is = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream os = new BufferedOutputStream(s.getOutputStream());
		try{
			while (true) {
				byte buffer[] = new byte[4096];
				is.read(buffer);
				// might need to number the requests
				String token = new String(buffer).trim().replace("\n", "");
				if (super.isDebugging()) {
					System.out.println(token);
				}
				String response = ((Auth) super.getParentThread())
						.isValidToken(token);

				os.write(response.getBytes());
				os.flush();
			}
		} finally {
			s.close();
			serverSocket.close();
		}
	}

}
