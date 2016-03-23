package it.stasbranger.rotarylive.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.stasbranger.rotarylive.domain.Location;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

}
