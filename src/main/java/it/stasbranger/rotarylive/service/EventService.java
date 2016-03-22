package it.stasbranger.rotarylive.service;

import java.util.List;

import it.stasbranger.rotarylive.domain.Event;

public interface EventService {
	
	public void create(Event event);
	public Event update(Event event);
	public Event findOne(String id);
	public List<Event> findAll();
	public void delete(String id);
	public void delete(Event event);
	
	public List<Event> clientNotification();
}
