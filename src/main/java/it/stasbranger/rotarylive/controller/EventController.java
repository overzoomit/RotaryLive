package it.stasbranger.rotarylive.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.stasbranger.rotarylive.domain.Event;
import it.stasbranger.rotarylive.domain.User;
import it.stasbranger.rotarylive.resource.EventResource;
import it.stasbranger.rotarylive.resource.EventResourceAssembler;
import it.stasbranger.rotarylive.service.EventService;
import it.stasbranger.rotarylive.service.UserService;

@RestController
@RequestMapping("/api/event")
@ExposesResourceFor(Event.class)
public class EventController {

	@Autowired private EventService eventService;
	@Autowired private UserService userService;
	@Autowired private EventResourceAssembler eventResourceAssembler;

	@RequestMapping(value = "/timeline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public HttpEntity<PagedResources<EventResource>> showEventsTimeline(@RequestParam(value = "q", required = false) String query, @RequestParam(value = "d1", required = false) Date date1, @RequestParam(value = "d2", required = false) Date date2, @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC, sort = "startDate") Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		Page<Event> events = this.eventService.findByDate(query, date1, date2, pageable);
		PagedResources<EventResource> resources = assembler.toResource(events, eventResourceAssembler);
		return new ResponseEntity<PagedResources<EventResource>>(resources, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/calendar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public HttpEntity<PagedResources<EventResource>> showEventsCalendar(@RequestParam(value = "q", required = false) String query, @RequestParam(value = "d", required = false) Date date, @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC, sort = "startDate") Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		Page<Event> events = this.eventService.findAll(query, date, pageable);
		PagedResources<EventResource> resources = assembler.toResource(events, eventResourceAssembler);
		return new ResponseEntity<PagedResources<EventResource>>(resources, HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<Event>> createEvent(@RequestBody Event event) {
		this.eventService.create(event);
		return new ResponseEntity<Resources<Event>>(HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<Event>> deleteEvent(@PathVariable ObjectId id) {
		if(this.eventService.findOne(id) == null){
			return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
		}
		this.eventService.delete(id);
		return new ResponseEntity<Resource<Event>>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/booking", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<Event>> bookingEvent(@PathVariable("id") ObjectId id, @RequestParam(value = "comment", required = false) String comment, HttpServletRequest httpServletRequest) {
		try {
			Event event = eventService.findOne(id);
			if (event == null) {
				return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
			}
			String username = httpServletRequest.getUserPrincipal().getName();
			User user = userService.findByUsername(username);
			event = eventService.createBooking(event, user, comment);
			Resource<Event> resource = eventResourceAssembler.toResource(event);
			return new ResponseEntity<Resource<Event>>(resource, HttpStatus.CREATED);
		} catch(DuplicateKeyException e){
			return new ResponseEntity<Resource<Event>>(HttpStatus.CONFLICT);	
		} catch (Exception e) {
			return new ResponseEntity<Resource<Event>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/{id}/booking", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<Event>> removeBookingEvent(@PathVariable("id") ObjectId id, HttpServletRequest httpServletRequest) {
		try {
			Event event = eventService.findOne(id);
			if (event == null) {
				return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
			}
			String username = httpServletRequest.getUserPrincipal().getName();
			User user = userService.findByUsername(username);
			event = eventService.removeBooking(event, user);
			Resource<Event> resource = eventResourceAssembler.toResource(event);
			return new ResponseEntity<Resource<Event>>(resource, HttpStatus.OK);
		} catch(NotFoundException e){
			return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);	
		} catch (Exception e) {
			return new ResponseEntity<Resource<Event>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/{id}/booking/{uid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<Event>> bookingEventByUserId(@PathVariable("id") ObjectId id, @PathVariable("uid") ObjectId uid, @RequestParam(value = "comment", required = false) String comment, HttpServletRequest httpServletRequest) {
		try {
			Event event = eventService.findOne(id);
			if (event == null) {
				return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
			}
			User user = userService.findOne(uid);
			if (user == null) {
				return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
			}
			event = eventService.createBooking(event, user, comment);
			Resource<Event> resource = eventResourceAssembler.toResource(event);
			return new ResponseEntity<Resource<Event>>(resource, HttpStatus.OK);
		} catch(DuplicateKeyException e){
			return new ResponseEntity<Resource<Event>>(HttpStatus.CONFLICT);	
		} catch (Exception e) {
			return new ResponseEntity<Resource<Event>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/{id}/booking/{uid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<Event>> removeBookingEventByUserId(@PathVariable("id") ObjectId id, @PathVariable("uid") ObjectId uid, HttpServletRequest httpServletRequest) {
		try {
			Event event = eventService.findOne(id);
			if (event == null) {
				return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
			}
			User user = userService.findOne(uid);
			if (user == null) {
				return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);
			}
			event = eventService.removeBooking(event, user);
			Resource<Event> resource = eventResourceAssembler.toResource(event);
			return new ResponseEntity<Resource<Event>>(resource, HttpStatus.OK);
		} catch(NotFoundException e){
			return new ResponseEntity<Resource<Event>>(HttpStatus.NOT_FOUND);	
		} catch (Exception e) {
			return new ResponseEntity<Resource<Event>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
