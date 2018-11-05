package com.jpmorgan.service;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

import org.springframework.stereotype.Service;

import com.jpmorgan.beans.UserCredentials;
import com.jpmorgan.beans.UserToken;
import com.jpmorgan.util.StringHasher;

@Service
public class AuthenticationService {
	
	/** A singleton, ordered set that provides concurrent access. It is 
	 * sorted based on the expiration. */
	static ConcurrentSkipListSet<UserToken> sessions = new ConcurrentSkipListSet<>(
			(UserToken a, UserToken b) -> a.getInvalidateTime().compareTo(b.getInvalidateTime()));
	
	static Object lock = new Object();
	
	/** The number of seconds until a session expires. */
	//TODO: Not hard code this value. Should be pulled proper from .yml file
	private static long EXPIRATION_SECONDS = 600;
	
	public UserToken login(UserCredentials userCredentials) {
		removeExpiredSessions();
		
		//TODO: Test credentials
		if (true) {
			//TODO: Implement session saving. 
			//TODO: Add Better Random Tokens, as well as implement expiration date. 
			UserToken userToken = new UserToken(
					StringHasher.sha256Hash(
							userCredentials.getUsername() + userCredentials.getPassword()), 
					ZonedDateTime.now().plusSeconds(EXPIRATION_SECONDS));
			
			sessions.add(userToken);
			
			return userToken;
		}
		
		return null;
	}
	
	public void logout(UserToken userToken) {
		for (Iterator<UserToken> it = sessions.iterator(); it.hasNext();) {
			UserToken tempToken = it.next();
			
			if (tempToken.getUserTokenStr().equals(userToken.getUserTokenStr())) {
				it.remove();
//				tempToken.setInvalidateTime(ZonedDateTime.now().minusDays(1));
				
				break;
			}
		}
	}
	
	public boolean isValid(UserToken userToken) {
		removeExpiredSessions();
		
		System.out.println("Sessions: " + sessions);
		System.out.println("Is Valid?: " + userToken);
		System.out.println("IsValid: " + sessions.contains(userToken));
		
		for (Iterator<UserToken> it = sessions.iterator(); it.hasNext();) {
			UserToken tempToken = it.next();
			if (tempToken.getUserTokenStr().equals(userToken.getUserTokenStr())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes sessions that have expired. 
	 */
	private void removeExpiredSessions() {
		ZonedDateTime currentTime = ZonedDateTime.now();
		
		UserToken session;
		
		// Remove sessions while there are sessions that 
		while (!sessions.isEmpty() && sessions.first().getInvalidateTime().compareTo(currentTime) < 0) {
			session = sessions.pollFirst();
			// Due to concurrent access, more than one element might be removed. 
			// To handle this, if an element is removed that hasn't expired, it 
			// is put back. 
			if (session.getInvalidateTime().compareTo(currentTime) >= 0) {
				sessions.add(session);
			}
		}
	}
	
	/** Removes all sessions from the service. Primarily for testing. */
	public void logoutAll() {
		System.out.println("Called logoutAll. Should only for testing. ");
		sessions.clear();
	}
	
	/** Sets the time until a new session expires in seconds. */
	public void setExpiration(long timeoutSeconds) {
		EXPIRATION_SECONDS = timeoutSeconds;
	}
	
}
