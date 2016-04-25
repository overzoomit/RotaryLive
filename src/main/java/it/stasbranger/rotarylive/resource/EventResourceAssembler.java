package it.stasbranger.rotarylive.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import it.stasbranger.rotarylive.controller.EventController;
import it.stasbranger.rotarylive.domain.Event;

@Component
public class EventResourceAssembler extends ResourceAssemblerSupport<Event, EventResource> {

	public EventResourceAssembler() {
		super(EventController.class, EventResource.class);
	}

	@Override
	public List<EventResource> toResources(Iterable<? extends Event> events) {
		List<EventResource> resources = new ArrayList<EventResource>();
		for(Event event : events) {
			resources.add(new EventResource(event, linkTo(EventController.class).slash(event.getId()).withSelfRel()));
		}
		return resources;
	}

	@Override
	public EventResource toResource(Event event) {
		return new EventResource(event, linkTo(EventController.class).slash(event.getId()).withSelfRel());
	}
}
