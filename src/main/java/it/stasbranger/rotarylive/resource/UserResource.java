package it.stasbranger.rotarylive.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import it.stasbranger.rotarylive.domain.User;

public class UserResource extends Resource<User>{
	public UserResource(User content, Link links) {
		super(content, links);
	}
}
