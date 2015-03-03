package com.sap.on.ibm.i.editor.model;


public class User {

	private String user;
	private String password;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserData() {

		String userData = " -pw " + getPassword();

		userData += " " + getUser();

		return userData;

	}
	
}
