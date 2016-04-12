package it.stasbranger.rotarylive.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import it.stasbranger.rotarylive.domain.Club;

public class ClubResource extends Resource<Club>{
	public ClubResource(Club content, Link links) {
		super(content, links);
	}
}
