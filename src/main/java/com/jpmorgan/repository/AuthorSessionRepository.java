package com.jpmorgan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jpmorgan.beans.Author;
import com.jpmorgan.beans.AuthorSession;

@Repository
@Transactional
public interface AuthorSessionRepository extends JpaRepository<AuthorSession, String> {

	public AuthorSession findByauthorSessionToken(String token);

	public AuthorSession findByAuthor(Author author);

	public boolean existsByauthorSessionToken(String token);
}
