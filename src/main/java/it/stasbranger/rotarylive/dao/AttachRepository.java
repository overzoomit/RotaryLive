package it.stasbranger.rotarylive.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Attach;

public interface AttachRepository extends MongoRepository<Attach, String> {

	public Attach findByUriCode(String uriCode);
}
