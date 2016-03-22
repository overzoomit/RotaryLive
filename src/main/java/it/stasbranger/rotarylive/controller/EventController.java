package it.stasbranger.rotarylive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.stasbranger.rotarylive.domain.Event;
import it.stasbranger.rotarylive.service.EventService;

@RestController
@RequestMapping("/api/event")
@ExposesResourceFor(Event.class)
public class EventController {

	@Autowired
	private EventService eventService;

	private final EntityLinks entityLinks;

	@Autowired
	public EventController(EventService eventService, EntityLinks entityLinks) {
		this.eventService = eventService;
		this.entityLinks = entityLinks;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<Event>> showEvent(@PathVariable String id) {
		Event event = this.eventService.findOne(id);
		if(event == null){
			return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
		}
		Resource<Event> resource = new Resource<Event>(event);
		resource.add(this.entityLinks.linkToSingleResource(Event.class, id));
		return new ResponseEntity<Resource<Event>>(resource, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<Event>> createEvent(@RequestBody Event event) {
		this.eventService.create(event);
		return new ResponseEntity<Resources<Event>>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<Event>> updateEvent(@PathVariable String id, @RequestBody Event event) {
		event.setId(id);
		
		if(this.eventService.findOne(id) == null){
			return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
		}
		event = this.eventService.update(event);
		Resource<Event> resource = new Resource<Event>(event);
		resource.add(this.entityLinks.linkToSingleResource(Event.class, id));
		return new ResponseEntity<Resource<Event>>(resource, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<Event>> deleteEvent(@PathVariable String id) {
		if(this.eventService.findOne(id) == null){
			return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
		}
		this.eventService.delete(id);
		return new ResponseEntity<Resource<Event>>(HttpStatus.OK);
	}
}
