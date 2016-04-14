package it.stasbranger.rotarylive.dao;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Club;

public interface ClubRepository extends MongoRepository<Club, ObjectId> {

	public Page<Club> findByNameContainingIgnoreCase(String name, Pageable pageable);
	
	public Club findByName(String name);
}
