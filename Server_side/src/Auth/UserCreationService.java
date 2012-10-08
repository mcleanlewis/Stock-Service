package Auth;

import helpers.Configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;

import javax.net.ServerSocketFactory;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.digest.DigestUtils;

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
			System.err.println("userCreation Started");
		}
		while (true) {
			try {
				User temp = listenForRequest();
				((Auth) super.getParentThread()).addUser(temp);
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
		String client = new String(buffer).trim().replace("\n", "");
		if (super.isDebugging()) {
			System.out.println(client);
		}

		User account = new User();
		account.setName(client);
		account.setPassword(encrypt("this isn't good security", client));
		account.setSecret(generateSecret());
		account.setCreated(new Date());

		String response = getQRBarcodeURL(account.getName(), "sockets_test",
				account.getSecret());

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

	public static String encrypt(String password, String salt) {
		return DigestUtils.shaHex(password + salt);
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

