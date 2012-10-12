package Auth;

import helpers.Configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private boolean isDebugging = false;

	public Auth(Configuration configuration) {

		this.configuration = configuration;
		dataStore = getDataStore();
		if (configuration.getProperty("DEBUG").equals("TRUE")) {
			isDebugging = true;
		}
		if (isDebugging) {
			System.err.println(this.toString() + " istantiated");
		}
		// would be separate services
		startServerService();
		startRegThread();
		startTokenThread();
	}


	private HashMap<String, User> getDataStore() {
		ObjectInputStream in = null;

		try {
			FileInputStream fis = new FileInputStream("userData.data");
			in = new ObjectInputStream(fis);
			HashMap<String, User> temp = (HashMap<String, User>) in.readObject();
			return temp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new HashMap<String, User>();
	}

	@Override
	public void run() {

		if (isDebugging) {
			System.err.println(this.toString() + " Started");
		}



	}

	private void startServerService() {
		try {
			ServerService serverService = new ServerService(configuration, this);
			serverService.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		saveDataStore();
	}

	private void saveDataStore() {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream("userData.data");
			out = new ObjectOutputStream(fos);
			out.writeObject(dataStore);
			out.close();
			System.out.println("Object Persisted");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

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
			// System.err.println(token + "  " + new
			// String(user.getCurrentToken()));
			if (user.getCurrentToken() != null) {
				if (token.equals(new String(user.getCurrentToken()))) {
					if (user.getTokenExpiration() > System.currentTimeMillis()) {
						long timeleft = user.getTokenExpiration() - System.currentTimeMillis();
						System.out.println("TIME LEFT " + timeleft);
						return "TRUE";
					}
				}
			}
		}
		return "FALSE";
	}
}
