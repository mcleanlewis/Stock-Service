package Auth;

import helpers.Configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.NoSuchPaddingException;
import javax.net.ServerSocketFactory;

import org.apache.commons.codec.binary.Base32;

import Beans.User;

public class UserCreationService extends Service {

	private final int port;

	public UserCreationService(Configuration configuration, Auth auth)
			throws IOException {
		super(configuration, auth);
		port = Integer.parseInt(configuration.getProperty("USER_CREATION"));
	}

	@Override
	public void run() {

		if (super.isDebugging()) {
			System.err.println("user creation thread started");
		}
		while (true) {
			try {
				User temp = listenForRequest();
				if (null != temp) {
					((Auth) super.getParentThread()).addUser(temp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private User listenForRequest() throws IOException {

		ServerSocketFactory serverSocketFactory = ServerSocketFactory
				.getDefault();

		ServerSocket serverSocket = serverSocketFactory
				.createServerSocket(port);

		Socket s = serverSocket.accept();
		BufferedInputStream is = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream os = new BufferedOutputStream(s.getOutputStream());

		byte buffer[] = new byte[4096];
		is.read(buffer);
		String clientString = new String(buffer).trim().replace("\n", "");
		String client = clientString.split("><")[0];
		String password = clientString.split("><")[1]; // this isn't secure -shouldn't use object from VM string pool


		String response = "ERROR:Username exists already";
		User account = null;

		if (!((Auth) super.getParentThread()).userExists(client)) {


			account = new User();
			account.setName(client);
			try {
				account.setPassword(TokenFactory.encrypt("this isn't good security", password));
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			account.setSecret(generateSecret());
			account.setCreated(new Date());

			response = getQRBarcodeURL(account.getName(), "sockets_test",
					account.getSecret());

			if (super.isDebugging()) {
				System.out.println("Registered " + client);
			}
		}
		os.write(response.getBytes());
		os.flush();
		s.close();
		serverSocket.close();

		return account;
	}

	public static String getQRBarcodeURL(String user, String host, String secret) {
		String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
		return String.format(format, user, host, secret);
	}

	public String generateSecret() {

		// Allocating the buffer
		byte[] buffer = new byte[super.getSECRET_SIZE()];

		// Filling the buffer with random numbers.
		super.getRand().nextBytes(buffer);

		// Getting the key and converting it to Base32
		Base32 codec = new Base32();
		byte[] secretKey = Arrays.copyOf(buffer, super.getSECRET_SIZE());
		byte[] encodedKey = codec.encode(secretKey);
		return new String(encodedKey);
	}

}

