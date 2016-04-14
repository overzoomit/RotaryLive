package it.stasbranger.rotarylive.dao;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.stasbranger.rotarylive.domain.User;


public interface UserRepository extends MongoRepository<User, ObjectId> {

	public User findByUsername(String username);
	
	@Query("{$text: {$search: ?0}}")
	public Page<User> findAll(String query, Pageable pageable);
}