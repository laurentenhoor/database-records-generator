package com.topdesk.si2011.dbgenerator.communication;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {

	// This method is called when a password-protected URL is accessed
	protected PasswordAuthentication getPasswordAuthentication() {
		// Get the username from the user...
		String username = "admin";

		// Get the password from the user...
		String password = "admin";

		// Return the information
		return new PasswordAuthentication(username, password.toCharArray());
	}
}
