package it.stasbranger.rotarylive.service;

import java.util.List;

import org.bson.types.ObjectId;

import it.stasbranger.rotarylive.domain.Booking;

public interface BookingService {

	public void create(Booking booking);
	public Booking update(Booking booking);
	public Booking findOne(ObjectId id);
	public List<Booking> findAll();
	public void delete(ObjectId id);
	public void delete(Booking booking);
}
