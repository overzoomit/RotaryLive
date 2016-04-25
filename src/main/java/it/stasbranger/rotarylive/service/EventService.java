package it.stasbranger.rotarylive.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.stasbranger.rotarylive.domain.Event;
import it.stasbranger.rotarylive.domain.User;

public interface EventService {
	
	public void create(Event event);
	public Event update(Event event);
	public Event findOne(ObjectId id);
	public List<Event> findAll();
	public void delete(ObjectId id);
	public void delete(Event event);
	public Page<Event> findAll(Pageable pageable);
	public Page<Event> findAll(String query, Date date, Pageable pageable);
	public Page<Event> findByDate(String query, Date date1, Date date2, Pageable pageable);
	public List<Event> clientNotification();
	public Event addImage(Event event) throws IOException;
	public Event createBooking(Event event, User user, String comment) throws Exception;
	public Event removeBooking(Event event, User user) throws Exception;
}
