package it.stasbranger.rotarylive.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.User;


public interface UserRepository extends MongoRepository<User, String> {

	public User findByLogin(String login);
}