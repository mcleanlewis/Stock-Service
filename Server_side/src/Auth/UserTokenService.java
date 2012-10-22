package Auth;

import helpers.Configuration;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;
import javax.net.ServerSocketFactory;

import org.apache.commons.codec.binary.Base32;

import Beans.User;
import TwoFactorAuth.TOTP;

public class UserTokenService extends Service {

	private final int port;

	public UserTokenService(Configuration configuration, Auth auth)
			throws IOException {
		super(configuration, auth);
		port = Integer.parseInt(configuration.getProperty("USER_TOKEN"));
	}

	@Override
	public void run() {

		if (super.isDebugging()) {
			System.err.println(this.toString() + "user token thread Started");
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
	NumberFormatException,
	NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
	InvalidAlgorithmParameterException {

		String token = null;
		Socket s = null;
		ServerSocket serverSocket = null;
		try {

			ServerSocketFactory serverSocketFactory = ServerSocketFactory
					.getDefault();

			serverSocket = serverSocketFactory
					.createServerSocket(port);

			s = serverSocket.accept();
			// BufferedInputStream is = new
			// BufferedInputStream(s.getInputStream());
			DataInputStream is = new DataInputStream(s.getInputStream());
			BufferedOutputStream os = new BufferedOutputStream(s.getOutputStream());

			byte buffer[] = new byte[4096];
			String request = is.readLine();
			// String request = new String(buffer).trim().replace("\n", "");
			if (super.isDebugging()) {
				System.out.println(request);
			}
			String[] requestArray = request.split("><"); // [user,code]

			if (requestArray.length == 3) {
				// issue token
				// check if user exists
				if(((Auth) super.getParentThread()).userExists(requestArray[0])){
					User currentUser = ((Auth) super.getParentThread())
							.getUser(requestArray[0]);
					// check password
					if (((Auth) super.getParentThread()).getUser(requestArray[0]).getPassword()
							.equals(TokenFactory.encrypt("this isn't good security", requestArray[1]))) {
						// check code
						if (checkCode(currentUser.getSecret(), Long.parseLong(requestArray[2]))) {
							// issue token
							token = setUserToken(currentUser);
						} else {
							token = "invalid token";
						}
					} else {
						token = "invalid password";
					}
				} else {
					token = "invalid user";
				}

			} else if (requestArray.length == 2) {
				// renew token
				if (((Auth) super.getParentThread()).userExists(requestArray[0])) {
					User currentUser = ((Auth) super.getParentThread()).getUser(requestArray[0]);

					if (new String(currentUser.getCurrentToken()).equals(requestArray[1])
							&& ((currentUser.getTokenExpiration() + 10000) > System
									.currentTimeMillis())) {
						token = setUserToken(currentUser);
					} else {
						token = "ERROR : malformed, erronous or expired previous token";
					}
				}
			}

			os.write(token.getBytes());
			os.flush();

		} finally {
			s.close();
			serverSocket.close();
			token = null; // for GC
		}
	}

	private String setUserToken(User currentUser) {
		String token;
		token = TokenFactory.getToken(currentUser);
		currentUser.setCurrentToken(token.toCharArray());
		currentUser.setTokenExpiration(System.currentTimeMillis() + 30000); // access for just over 2 minutes
		return token;
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
