package it.stasbranger.rotarylive.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {

}
