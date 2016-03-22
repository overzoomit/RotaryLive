package it.stasbranger.rotarylive.service;

import java.util.List;

import it.stasbranger.rotarylive.domain.Booking;

public interface BookingService {

	public void create(Booking booking);
	public Booking update(Booking booking);
	public Booking findOne(String id);
	public List<Booking> findAll();
	public void delete(String id);
	public void delete(Booking booking);
}
