package com.jpmorgan.service;

import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpmorgan.beans.Author;
import com.jpmorgan.beans.AuthorSession;
import com.jpmorgan.beans.UserCredentials;
import com.jpmorgan.beans.UserToken;
import com.jpmorgan.repository.AuthorRepository;
import com.jpmorgan.repository.AuthorSessionRepository;
import com.jpmorgan.util.StringHasher;

import oracle.sql.DATE;

@Service
public class AuthenticationService {
	
	/** A singleton, ordered set that provides concurrent access. It is 
	 * sorted based on the expiration. */
	static ConcurrentSkipListSet<UserToken> sessionsByTime = new ConcurrentSkipListSet<>(
			(UserToken a, UserToken b) -> a.getInvalidateTime().compareTo(b.getInvalidateTime()));
	
	static ConcurrentSkipListMap<UserToken,UserToken> sessionsByToken = new ConcurrentSkipListMap<>(
			(UserToken a, UserToken b) -> a.getUserTokenStr().compareTo(b.getUserTokenStr()));
	
	static Object lock = new Object();
	
	
	SecureRandom sRand = new SecureRandom();
	
	
	@Autowired
	AuthorRepository authorRepo;
	
	@Autowired
	AuthorSessionRepository sessionRepo;
	
	
	/** The number of seconds until a session expires. */
	//TODO: Not hard code this value. Should be pulled proper from .yml file
	private static long EXPIRATION_SECONDS = 600;
	
	
	private static final int BYTES_IN_RANDOM = 256/8;
	
	public UserToken login(UserCredentials userCredentials) {
//		removeExpiredSessions();
		
		if (userCredentials == null) {
			// TODO: Log incorrect response
			return null;
		}
		
		if (authorRepo.existsByUsernameAndPassword(
				userCredentials.getUsername(), 
				StringHasher.sha256Hash(userCredentials.getPassword())))
		{
			Author author = authorRepo.findByUsername(userCredentials.getUsername());
			
			//TODO: Replace with a better date class, like timezone based date
			byte[] randBytes = new byte[BYTES_IN_RANDOM];
			sRand.nextBytes(randBytes);
			String tokenString = StringHasher.sha256Hash(new String(randBytes));
			sessionRepo.save(new AuthorSession(tokenString, Date.valueOf(LocalDate.now().plusDays(1)), author));
			return new UserToken(tokenString, ZonedDateTime.now().plusDays(1));
		}
		
		return null;
	}
	
	public void logout(UserToken userToken) {
		sessionRepo.delete(new AuthorSession(userToken.getUserTokenStr(), null, null));
	}
	
	public boolean isValid(UserToken userToken) {
		//TODO: Add date-time based validity control. 
		return sessionRepo.existsByauthorSessionToken(userToken.getUserTokenStr());
	}
	
	/**
	 * Removes sessions that have expired. 
	 */
	private void removeExpiredSessions() {
		ZonedDateTime currentTime = ZonedDateTime.now();
		
		UserToken session;
		
		synchronized(lock) {
		// Remove sessions while there are sessions that 
		while (!sessionsByTime.isEmpty() && sessionsByTime.first().getInvalidateTime().compareTo(currentTime) < 0) {
			session = sessionsByTime.pollFirst();
			sessionsByToken.remove(session);
//			// Due to concurrent access, more than one element might be removed. 
//			// To handle this, if an element is removed that hasn't expired, it 
//			// is put back. 
//			if (session.getInvalidateTime().compareTo(currentTime) >= 0) {
//				sessions.add(session);
//			}
		}
		}
	}
	
	/** Removes all sessions from the service. Primarily for testing. */
	public void logoutAll() {
		System.out.println("Called logoutAll. Should only for testing. ");
		sessionsByTime.clear();
		sessionsByToken.clear();
		
		List<AuthorSession> sessions = sessionRepo.findAll();
		for (AuthorSession session : sessions) {
			sessionRepo.delete(session);
		}
	}
	
	/** Sets the time until a new session expires in seconds. */
	public void setExpiration(long timeoutSeconds) {
		EXPIRATION_SECONDS = timeoutSeconds;
	}
	
}
