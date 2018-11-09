package com.jpmorgan.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Author {

	@Id
	@Column(name = "AuthorID")
	@SequenceGenerator(name = "AuthorID", sequenceName = "AuthorID")
	@GeneratedValue(generator = "AuthorID", strategy = GenerationType.SEQUENCE)
	private int authorID;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String username;

	
	@Column(nullable = false)
	private String email;
	

	public Author() {
		
	}

	public int getAuthorID() {
		return authorID;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
}