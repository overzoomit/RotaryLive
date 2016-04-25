package it.stasbranger.rotarylive.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import it.stasbranger.rotarylive.domain.Attach;

public class AttachResource extends Resource<Attach>{
	public AttachResource(Attach content, Link links) {
		super(content, links);
	}
}
