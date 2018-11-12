package com.jpmorgan.beans;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotEmpty;

/**
 * This class is used to send and receive user tokens. 
 */
public class UserToken implements Comparable<UserToken>{
	
	@NotEmpty
	private String userTokenStr;
	
	private ZonedDateTime invalidateTime;

	public UserToken() {
		super();
	}

	public UserToken(@NotEmpty String userTokenStr, @NotEmpty ZonedDateTime invalidateTime) {
		super();
		this.userTokenStr = userTokenStr;
		this.invalidateTime = invalidateTime;
	}

	public String getUserTokenStr() {
		return userTokenStr;
	}

	public void setUserTokenStr(String userTokenStr) {
		this.userTokenStr = userTokenStr;
	}

	public ZonedDateTime getInvalidateTime() {
		return invalidateTime;
	}

	public void setInvalidateTime(ZonedDateTime invalidateTime) {
		this.invalidateTime = invalidateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// Only the user token string should be factored into the hash code
		result = prime * result + ((userTokenStr == null) ? 0 : userTokenStr.hashCode());
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
		UserToken other = (UserToken) obj;
		
		// Equality is only based on the user token string
		if (userTokenStr == null) {
			if (other.userTokenStr != null)
				return false;
		} else if (!userTokenStr.equals(other.userTokenStr)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UserToken [userTokenStr=" + userTokenStr + ", invalidateTime=" + invalidateTime + "]";
	}

	
	@Override
	public int compareTo(UserToken o) {
		return this.invalidateTime.compareTo(o.invalidateTime);
	}

	
	
	
}
