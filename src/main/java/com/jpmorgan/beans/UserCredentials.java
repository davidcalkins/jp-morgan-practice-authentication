package com.jpmorgan.beans;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Component;

@Component
public class UserCredentials {
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;

	public UserCredentials(@NotEmpty String username, @NotEmpty String password) {
		super();
		this.username = username;
		this.password = password;
	}

	/** 
	 * A blank constructor for spring to use for initialization. 
	 */
	public UserCredentials() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCredentials other = (UserCredentials) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password)) {
			return false;
		}
		// As this is the last field comparison, we can return the null check directly
		if (username == null) {
			return other.username == null;
		}
		
		return username.equals(other.username);
	}

	@Override
	public String toString() {
		return "UsernamePassword [username=" + username + ", password=" + password + "]";
	}
	
	
	
	
}
