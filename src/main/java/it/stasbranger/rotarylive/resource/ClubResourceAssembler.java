package it.stasbranger.rotarylive.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import it.stasbranger.rotarylive.controller.ClubController;
import it.stasbranger.rotarylive.domain.Club;

@Component
public class ClubResourceAssembler extends ResourceAssemblerSupport<Club, ClubResource> {

	public ClubResourceAssembler() {
		super(ClubController.class, ClubResource.class);
	}

	@Override
	public List<ClubResource> toResources(Iterable<? extends Club> clubs) {
		List<ClubResource> resources = new ArrayList<ClubResource>();
		for(Club club : clubs) {
			resources.add(new ClubResource(club, linkTo(ClubController.class).slash(club.getId()).withSelfRel()));
		}
		return resources;
	}

	@Override
	public ClubResource toResource(Club club) {
		return new ClubResource(club, linkTo(ClubController.class).slash(club.getId()).withSelfRel());
	}
}
