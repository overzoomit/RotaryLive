package it.stasbranger.rotarylive.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Event;

public interface EventRepository extends MongoRepository<Event, String> {

}
