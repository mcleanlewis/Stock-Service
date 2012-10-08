package Auth;

import helpers.Configuration;

import java.io.IOException;
import java.util.Random;

public class Service extends Thread {

	private final Configuration configuration;
	private boolean isDebugging = false;
	private final Thread parentThread;
	private final Random rand;
	private static int PASS_CODE_LENGTH;
	private static int INTERVAL;
	private static int WINDOW;
	private static String CRYPTO;
	private static int SECRET_SIZE;


	public Service(Configuration configuration, Thread parent)
			throws IOException {
		this.configuration = configuration;
		if (this.configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}
		PASS_CODE_LENGTH = Integer.parseInt(configuration
				.getProperty("PASS_CODE_LENGTH"));
		INTERVAL = Integer.parseInt(configuration.getProperty("INTERVAL"));
		WINDOW = Integer.parseInt(configuration.getProperty("WINDOW"));
		SECRET_SIZE = Integer
				.parseInt(configuration.getProperty("SECRET_SIZE"));
		CRYPTO = configuration.getProperty("CRYPTO");
		parentThread = parent;
		rand = new Random();
	}

	/**
	 * @return the isDebugging
	 */
	public boolean isDebugging() {
		return isDebugging;
	}

	/**
	 * @param isDebugging
	 *            the isDebugging to set
	 */
	public void setDebugging(boolean isDebugging) {
		this.isDebugging = isDebugging;
	}

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}


	/**
	 * @return the parentThread
	 */
	public Thread getParentThread() {
		return parentThread;
	}

	/**
	 * @return the rand
	 */
	public Random getRand() {
		return rand;
	}

	/**
	 * @return the pASS_CODE_LENGTH
	 */
	public static int getPASS_CODE_LENGTH() {
		return PASS_CODE_LENGTH;
	}

	/**
	 * @param pASS_CODE_LENGTH
	 *            the pASS_CODE_LENGTH to set
	 */
	public static void setPASS_CODE_LENGTH(int pASS_CODE_LENGTH) {
		PASS_CODE_LENGTH = pASS_CODE_LENGTH;
	}

	/**
	 * @return the iNTERVAL
	 */
	public static int getINTERVAL() {
		return INTERVAL;
	}

	/**
	 * @param iNTERVAL
	 *            the iNTERVAL to set
	 */
	public static void setINTERVAL(int iNTERVAL) {
		INTERVAL = iNTERVAL;
	}

	/**
	 * @return the wINDOW
	 */
	public static int getWINDOW() {
		return WINDOW;
	}

	/**
	 * @param wINDOW
	 *            the wINDOW to set
	 */
	public static void setWINDOW(int wINDOW) {
		WINDOW = wINDOW;
	}

	/**
	 * @return the cRYPTO
	 */
	public static String getCRYPTO() {
		return CRYPTO;
	}

	/**
	 * @param cRYPTO
	 *            the cRYPTO to set
	 */
	public static void setCRYPTO(String cRYPTO) {
		CRYPTO = cRYPTO;
	}

	/**
	 * @return the sECRET_SIZE
	 */
	public static int getSECRET_SIZE() {
		return SECRET_SIZE;
	}

	/**
	 * @param sECRET_SIZE
	 *            the sECRET_SIZE to set
	 */
	public static void setSECRET_SIZE(int sECRET_SIZE) {
		SECRET_SIZE = sECRET_SIZE;
	}
}
