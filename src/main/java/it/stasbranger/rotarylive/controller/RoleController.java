package it.stasbranger.rotarylive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.stasbranger.rotarylive.domain.Role;
import it.stasbranger.rotarylive.service.RoleService;

@RestController
@RequestMapping("/api/role")
@ExposesResourceFor(Role.class)
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<Role>> createRole(@RequestBody Role role) {
		this.roleService.create(role);
		return new ResponseEntity<Resources<Role>>(HttpStatus.CREATED);
	}
}
