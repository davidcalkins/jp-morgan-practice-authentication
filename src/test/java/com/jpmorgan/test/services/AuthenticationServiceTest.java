package com.jpmorgan.test.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.jpmorgan.AuthenticationApplication;
import com.jpmorgan.beans.UserCredentials;
import com.jpmorgan.beans.UserToken;
import com.jpmorgan.service.AuthenticationService;


@SpringBootTest(classes = AuthenticationApplication.class)
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class AuthenticationServiceTest {
	
	
	/**
	 * The Class DislikeServiceImplTestContextConfiguration. This class is just for
	 * setting up mock service for testing
	 */
	@TestConfiguration
	static class DislikeServiceImplTestContextConfiguration {

		/**
		 * returns a mock of the Dislike service.
		 *
		 * @return the dislike service
		 */
		@Bean
		public 	AuthenticationService dislikeService() {
			return new 	AuthenticationService();
		}
	}
	
	@Autowired
	AuthenticationService authService;
	
	/** The entity manager. */
	@Autowired
	private TestEntityManager entityManager;

	
	
	@Before
	public void clearSessions() {
		authService.logoutAll();
	}
	
	@Before
	public void resetExpiration() {
		authService.setExpiration(600);
	}
	
	
	
	@Test
	public void basicLogin() {
		UserCredentials creds = new UserCredentials("qwer", "qwer");
		
		UserToken token = authService.login(creds);
		assertTrue(authService.isValid(token));
	}
	
	@Test
	public void basicLogout() {
		UserCredentials creds = new UserCredentials("zxcv", "zxcv");
		
		UserToken token = authService.login(creds);
		assertTrue(authService.isValid(token));
		
		authService.logout(token);
		
		assertFalse(authService.isValid(token));
	}
	
	@Test
	public void multiUserLoginLogout() {
		UserCredentials creds1 = new UserCredentials("1234", "1234");
		UserCredentials creds2 = new UserCredentials("qwer", "qwer");
		UserCredentials creds3 = new UserCredentials("zxcv", "zxcv");
		
		UserToken token1 = authService.login(creds1);
		UserToken token2 = authService.login(creds2);
		UserToken token3 = authService.login(creds3);
		
		assertTrue(authService.isValid(token1));
		assertTrue(authService.isValid(token2));
		assertTrue(authService.isValid(token3));
		
		authService.logout(token2);
		authService.logout(token1);
		authService.logout(token3);
		
		assertFalse(authService.isValid(token1));
		assertFalse(authService.isValid(token2));
		assertFalse(authService.isValid(token3));
	}
	
	
	@Ignore
	public void properExpireTime() {
		authService.setExpiration(1);
		
		UserCredentials creds = new UserCredentials("asdf", "qwer");
		
		UserToken token = authService.login(creds);
		
		assertTrue(authService.isValid(token));
		
		// Wait for session to expire. Since this is about a date time, 
		// and not thread management, this Thread.sleep call should be fine. 
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		
		assertFalse(authService.isValid(token));
	}

}
