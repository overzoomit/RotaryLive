package it.stasbranger.rotarylive.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import it.stasbranger.rotarylive.controller.UserController;
import it.stasbranger.rotarylive.domain.User;

@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {

	public UserResourceAssembler() {
		super(UserController.class, UserResource.class);
	}

	@Override
	public List<UserResource> toResources(Iterable<? extends User> users) {
		List<UserResource> resources = new ArrayList<UserResource>();
		for(User user : users) {
			resources.add(new UserResource(user, linkTo(UserController.class).slash(user.getId()).withSelfRel()));
		}
		return resources;
	}

	@Override
	public UserResource toResource(User user) {
		return new UserResource(user, linkTo(UserController.class).slash(user.getId()).withSelfRel());
	}
}
