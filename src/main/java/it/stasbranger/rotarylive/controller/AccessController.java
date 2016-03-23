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

import it.stasbranger.rotarylive.domain.User;
import it.stasbranger.rotarylive.service.UserService;

@RestController
@ExposesResourceFor(User.class)
public class AccessController {

	@Autowired private UserService userService;
	
	@RequestMapping(value="/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<User>> createUser(@RequestBody User user) {
		this.userService.create(user);
		return new ResponseEntity<Resources<User>>(HttpStatus.CREATED);
	}
}
