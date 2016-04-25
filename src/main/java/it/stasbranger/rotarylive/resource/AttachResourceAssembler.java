package it.stasbranger.rotarylive.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import it.stasbranger.rotarylive.controller.AttachController;
import it.stasbranger.rotarylive.domain.Attach;

@Component
public class AttachResourceAssembler extends ResourceAssemblerSupport<Attach, AttachResource> {

	public AttachResourceAssembler() {
		super(AttachController.class, AttachResource.class);
	}

	@Override
	public List<AttachResource> toResources(Iterable<? extends Attach> attachs) {
		List<AttachResource> resources = new ArrayList<AttachResource>();
		for(Attach attach : attachs) {
			resources.add(new AttachResource(attach, linkTo(AttachController.class).slash(attach.getId()).withSelfRel()));
		}
		return resources;
	}

	@Override
	public AttachResource toResource(Attach attach) {
		return new AttachResource(attach, linkTo(AttachController.class).slash(attach.getId()).withSelfRel());
	}
}
