package it.stasbranger.rotarylive.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Attach;

public interface AttachRepository extends MongoRepository<Attach, ObjectId> {
}
