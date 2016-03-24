package it.stasbranger.rotarylive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.stasbranger.rotarylive.domain.User;
import it.stasbranger.rotarylive.resource.UserResource;
import it.stasbranger.rotarylive.resource.UserResourceAssembler;
import it.stasbranger.rotarylive.service.UserService;

@RestController
@RequestMapping("/api/user")
@ExposesResourceFor(User.class)
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public HttpEntity<PagedResources<UserResource>> showUsers(@PageableDefault Pageable pageable, PagedResourcesAssembler<User> assembler) {
		Page<User> users = this.userService.findAll(pageable);
		PagedResources<UserResource> resources = assembler.toResource(users, userResourceAssembler);
		return new ResponseEntity<PagedResources<UserResource>>(resources, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<User>> showUser(@PathVariable String id) {
		try{
			User user = this.userService.findOne(id);
			if(user == null){
				return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
			}
			Resource<User> resource = userResourceAssembler.toResource(user);
			return new ResponseEntity<Resource<User>>(resource, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<Resource<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public HttpEntity<Resources<User>> createUser(@RequestBody User user) {
		try{
			this.userService.create(user);
			return new ResponseEntity<Resources<User>>(HttpStatus.CREATED);
		}catch(DuplicateKeyException e){
			return new ResponseEntity<Resources<User>>(HttpStatus.CONFLICT);	
		}catch(Exception e){
			return new ResponseEntity<Resources<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<User>> updateUser(@PathVariable String id, @RequestBody User user) {
		user.setId(id);

		if(this.userService.findOne(id) == null){
			return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
		}
		user = this.userService.update(user);
		Resource<User> resource = userResourceAssembler.toResource(user);
		return new ResponseEntity<Resource<User>>(resource, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<User>> deleteUser(@PathVariable String id) {
		if(this.userService.findOne(id) == null){
			return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
		}
		this.userService.delete(id);
		return new ResponseEntity<Resource<User>>(HttpStatus.OK);
	}
}
