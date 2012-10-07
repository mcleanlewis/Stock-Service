package Auth;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base32;

import Beans.Account;
import TwoFactorAuth.TOTP;

public class Auth extends Thread {

	private final ConcurrentHashMap<Account, Integer> accountMap;
	private static final int SECRET_SIZE = 10;

	private static final int PASS_CODE_LENGTH = 6;

	private static final int INTERVAL = 30;

	private static final int WINDOW = 30;

	private static final String CRYPTO = "HmacSHA1";

	private static final Random rand = new Random();

	public Auth(ConcurrentHashMap<Account, Integer> accountMap) {
		this.accountMap = accountMap;
	}


	@Override
	public void run() {

		// wait for request
		// check request
		// return response

	}

	public static boolean checkCode(String secret, long code)
			throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);

		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		int window = WINDOW;
		long currentInterval = getCurrentInterval();

		for (int i = -window; i <= window; ++i) {
			long hash = TOTP.generateTOTP(decodedKey, currentInterval + i,
					PASS_CODE_LENGTH, CRYPTO);

			if (hash == code) {
				return true;
			}
		}

		// The validation code is invalid.
		return false;
	}

	private static long getCurrentInterval() {
		long currentTimeSeconds = System.currentTimeMillis() / 1000;
		return currentTimeSeconds / INTERVAL;
	}

}
