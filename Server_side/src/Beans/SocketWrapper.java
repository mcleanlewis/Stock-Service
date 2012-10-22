package Beans;

import java.net.Socket;

public class SocketWrapper {

	private Socket	socket;
	private String	token;
	private long	tokenExpiration;

	public SocketWrapper(Socket socket, String token, String tokenExpiration) {
		this.socket = socket;
		this.token = token;
		this.tokenExpiration = Long.parseLong(tokenExpiration);
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket
	 *            the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the tokenExpiration
	 */
	public long getTokenExpiration() {
		return tokenExpiration;
	}

	/**
	 * @param tokenExpiration
	 *            the tokenExpiration to set
	 */
	public void setTokenExpiration(long tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}

}
