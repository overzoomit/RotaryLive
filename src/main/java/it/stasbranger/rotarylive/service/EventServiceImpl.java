package it.stasbranger.rotarylive.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.stasbranger.rotarylive.dao.EventRepository;
import it.stasbranger.rotarylive.domain.Event;

@Service("EventService")
public class EventServiceImpl implements EventService {

	@Autowired private EventRepository eventRepository;
	
	public void create(Event event){
		eventRepository.save(event);
	}
	
	public Event update(Event event){
		return eventRepository.save(event);
	}
	
	public void delete(Event event){
		eventRepository.delete(event);
	}
	
	public void delete(String id){
		eventRepository.delete(id);
	}
	
	public Event findOne(String id){
		return eventRepository.findOne(id);
	}
	
	public List<Event> findAll(){
		return eventRepository.findAll();
	}
	
	public List<Event> clientNotification(){
		 List<Event> events = findAll();
		 Iterator<Event> iterator = events.iterator();
		 while (iterator.hasNext()) {
			Event event = (Event) iterator.next();
			System.out.println(event.getId());
		}
		 
		return events;
	}
}
