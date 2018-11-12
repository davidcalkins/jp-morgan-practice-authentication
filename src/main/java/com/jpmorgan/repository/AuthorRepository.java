package com.jpmorgan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jpmorgan.beans.Author;


@Repository
@Transactional
public interface AuthorRepository extends JpaRepository<Author, Integer> {

	public Author findByAuthorID(int authorId);
	
	public Author findByUsername(String username);
	
	public boolean existsByUsernameAndPassword(String username, String password);
}
