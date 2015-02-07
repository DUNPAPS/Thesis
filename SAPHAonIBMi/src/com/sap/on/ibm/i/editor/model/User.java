/**
 * 
 */
package com.sap.on.ibm.i.editor.model;


/**
 * @author user
 * This class sets the username and Password
 *
 */
public class User {

	private User user;
	private String password;

	/**
	 * Constructor to initialise the username and password
	 * 
	 * @param user2
	 *            of username
	 * @param passord2
	 *            of password
	 */

	public User() {
	}


	/**
	 * Gets the user
	 * 
	 * @return user
	 */
	public User getUser() {
		if (this.user != null) {
			return this.user;
		} else {
			return null;
		}
	}

	/**
	 * Returns the user password
	 * 
	 * @return user password
	 */
	public Object getPassword() {
		if (this.password != null) {
			return this.password;
		} else {
			return null;
		}
	}


	public void setUser(User user) {
		this.user = user;
	}


	public void setPassword(String password) {
		this.password = password;
	}
}
