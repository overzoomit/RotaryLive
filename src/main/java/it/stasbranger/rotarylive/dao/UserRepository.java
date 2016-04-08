package it.stasbranger.rotarylive.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.User;


public interface UserRepository extends MongoRepository<User, ObjectId> {

	public User findByUsername(String username);
}