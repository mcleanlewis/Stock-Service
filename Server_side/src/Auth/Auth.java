package Auth;

import helpers.Configuration;

import java.io.IOException;
import java.util.HashMap;

import Beans.User;

/**
 * this would be separate services but for ease I've just made it a thread
 * 
 * @author lewismclean
 * 
 */
public class Auth extends Thread {

	private final Configuration configuration;
	private final HashMap<String, User> dataStore;

	public Auth(Configuration configuration) {

		this.configuration = configuration;
		dataStore = new HashMap<String, User>();
	}


	@Override
	public void run() {

		// would be separate services
		startRegThread();
		startTokenThread();
		startServerService();

	}

	private void startServerService() {
		// TODO Auto-generated method stub

	}

	private void startTokenThread() {
		UserTokenService tokenThread;
		try {
			tokenThread = new UserTokenService(configuration, this);
			tokenThread.start();
		} catch (IOException e) {
			// problem with sockets
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void startRegThread() {

		UserCreationService userThread = null;
		try {
			userThread = new UserCreationService(configuration, this);
			userThread.start();
		} catch (IOException e) {
			// problem with sockets
			e.printStackTrace();
			System.exit(1);
		}
	}

	public synchronized void addUser(User user) {
		dataStore.put(user.getName(), user);
	}

	public synchronized User getUser(String userName) {
		return dataStore.get(userName);
	}

	public synchronized boolean userExists(User user) {
		return userExists(user.getName());
	}

	public boolean userExists(String userName) {
		return (dataStore.get(userName) == null) ? false : true;

	}

	public String isValidToken(String token) {
		for (User user : dataStore.values()) {
			if (token.equals(user.getCurrentToken())) {
				if (user.getTokenExpiration() < System.currentTimeMillis()) {
					return "TRUE";
				}
			}
		}
		return "FALSE";
	}
}
