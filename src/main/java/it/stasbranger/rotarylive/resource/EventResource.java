package it.stasbranger.rotarylive.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import it.stasbranger.rotarylive.domain.Event;

public class EventResource extends Resource<Event>{
	public EventResource(Event content, Link links) {
		super(content, links);
	}
}
