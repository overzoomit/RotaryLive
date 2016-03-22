package it.stasbranger.rotarylive.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Location;

public interface LocationRepository extends MongoRepository<Location, String> {

}
