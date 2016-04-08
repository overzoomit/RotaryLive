package it.stasbranger.rotarylive.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Club;

public interface ClubRepository extends MongoRepository<Club, ObjectId> {

	public List<Club> findByNameLike(String name);
}
