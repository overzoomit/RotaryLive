package it.stasbranger.rotarylive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.stasbranger.rotarylive.dao.BookingRepository;
import it.stasbranger.rotarylive.domain.Booking;

@Service("BookingService")
public class BookingServiceImpl implements BookingService {
	
	@Autowired private BookingRepository bookingRepository;
	
	public void create(Booking booking){
		bookingRepository.save(booking);
	}
	
	public Booking update(Booking booking){
		return bookingRepository.save(booking);
	}
	
	public void delete(Booking booking){
		bookingRepository.delete(booking);
	}
	
	public void delete(String id){
		bookingRepository.delete(id);
	}
	
	public Booking findOne(String id){
		return bookingRepository.findOne(id);
	}
	
	public List<Booking> findAll(){
		return bookingRepository.findAll();
	}
}
