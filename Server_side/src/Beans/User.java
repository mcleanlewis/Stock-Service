package Beans;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8156912488983838145L;
	private long id;
	private String name;
	private String password;
	private String secret;
	private Date created;
	private long tokenExpiration;
	// strings are not secure
	private char[] currentToken;

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

	/**
	 * @return the currentToken
	 */
	public char[] getCurrentToken() {
		return currentToken;
	}

	/**
	 * @param currentToken
	 *            the currentToken to set
	 */
	public void setCurrentToken(char[] currentToken) {
		this.currentToken = currentToken;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}