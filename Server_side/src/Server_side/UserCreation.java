package Server_side;

import helpers.Configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.digest.DigestUtils;

import Beans.Account;

public class UserCreation extends Thread {

	private final Configuration configuration;
	private final int port;
	private boolean isDebugging = false;

	private static final int SECRET_SIZE = 10;
	private static final int PASS_CODE_LENGTH = 6;
	private static final int INTERVAL = 30;
	private static final int WINDOW = 30;
	private static final String CRYPTO = "HmacSHA1";
	private static final Random rand = new Random();
	private final ConcurrentHashMap<Account, Integer> accountMap;

	public UserCreation(Configuration configuration, ConcurrentHashMap<Account, Integer> accountMap) throws IOException {
		this.configuration = configuration;
		port = Integer.parseInt(configuration.getProperty("USER_CREATION"));
		if (configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}
		this.accountMap = accountMap;
	}

	@Override
	public void run() {

		if (isDebugging) {
			System.err.println("userCreation Started");
		}

		while (true) {
			try {
				Account temp = listenForRequest();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Account listenForRequest() throws IOException {

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
		if(isDebugging){
			System.out.println(client);
		}

		Account account = new Account();
		account.setName(client);
		account.setPassword(encrypt("this isn't good security", client));
		account.setSecret(generateSecret());
		account.setCreated(new Date());

		// qrCodeTicketRepository.createTicket(name);
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

	public static String generateSecret() {

		// Allocating the buffer
		byte[] buffer = new byte[SECRET_SIZE];

		// Filling the buffer with random numbers.
		rand.nextBytes(buffer);

		// Getting the key and converting it to Base32
		Base32 codec = new Base32();
		byte[] secretKey = Arrays.copyOf(buffer, SECRET_SIZE);
		byte[] encodedKey = codec.encode(secretKey);
		return new String(encodedKey);
	}

}

