package Auth;

import helpers.Configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.net.ServerSocketFactory;

import org.apache.commons.codec.binary.Base32;

import Beans.User;
import TwoFactorAuth.TOTP;

public class UserToken extends Service {

	private final int port;

	public UserToken(Configuration configuration, Auth auth)
			throws IOException {
		super(configuration, auth);
		port = Integer.parseInt(configuration.getProperty("USER_TOKEN"));
	}

	@Override
	public void run() {

		if (super.isDebugging()) {
			System.err.println("userAuth Started");
		}
		while (true) {
			try {
				listenForRequest();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void listenForRequest() throws IOException, InvalidKeyException,
	NumberFormatException, NoSuchAlgorithmException {

		ServerSocketFactory serverSocketFactory = ServerSocketFactory
				.getDefault();

		ServerSocket serverSocket = serverSocketFactory
				.createServerSocket(port);

		String token = null;
		Socket s = serverSocket.accept();
		BufferedInputStream is = new BufferedInputStream(s.getInputStream());
		BufferedOutputStream os = new BufferedOutputStream(s.getOutputStream());

		byte buffer[] = new byte[4096];
		is.read(buffer);
		String request = new String(buffer).trim().replace("\n", "");
		if (super.isDebugging()) {
			System.out.println(request);
		}
		String [] requestArray = request.split("_"); //[user,code]

		// check if user exisits
		if(((Auth) super.getParentThread()).userExists(requestArray[0])){
			User currentUser = ((Auth) super.getParentThread())
					.getUser(requestArray[0]);
			// check code
			if (checkCode(currentUser.getSecret(),
					Long.parseLong(requestArray[1]))) {
				// issue token
				token = TokenFactory.getToken(currentUser);
				currentUser.setCurrentToken(token.toCharArray());
				currentUser
				.setTokenExpiration(System.currentTimeMillis() + 600000); // access
				// for
				// 1hr
			} else {
				token = "invalid token";
			}
		} else {
			token = "invalid user";
		}

		os.write(token.getBytes());
		os.flush();
		s.close();
		serverSocket.close();
		token = null; // for GC

	}

	public boolean checkCode(String secret, long code)
			throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);

		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		int window = super.getWINDOW();
		long currentInterval = getCurrentInterval();

		for (int i = -window; i <= window; ++i) {
			long hash = TOTP.generateTOTP(decodedKey, currentInterval + i,
					super.getPASS_CODE_LENGTH(), super.getCRYPTO());

			if (hash == code) {
				return true;
			}
		}

		// The validation code is invalid.
		return false;
	}

	private long getCurrentInterval() {
		long currentTimeSeconds = System.currentTimeMillis() / 1000;
		return currentTimeSeconds / super.getINTERVAL();
	}


}
