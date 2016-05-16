package it.stasbranger.rotarylive.controller;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	public HttpEntity<PagedResources<UserResource>> showUsers(@RequestParam(value = "q", required = false) String query, @PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC, sort = "name") Pageable pageable, PagedResourcesAssembler<User> assembler, HttpServletRequest httpServletRequest) {
		if(httpServletRequest.isUserInRole("ADMIN")){
			System.out.println(httpServletRequest.getUserPrincipal().getName() + " IS ADMIN");
		}		
		
		Page<User> users = (query == null || query.trim().equals("")) ? this.userService.findAll(pageable) : this.userService.findAll(query, pageable);
		PagedResources<UserResource> resources = assembler.toResource(users, userResourceAssembler);
		return new ResponseEntity<PagedResources<UserResource>>(resources, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<User>> showUser(@PathVariable ObjectId id) {
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

	@RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<User>> showPrincipal(HttpServletRequest httpServletRequest) {
		try{
			String username = httpServletRequest.getUserPrincipal().getName();
			User user = this.userService.findByUsername(username);
			if(user == null){
				return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
			}
			if(!user.getVerified()){
				return new ResponseEntity<Resource<User>>(HttpStatus.LOCKED);
			}
			
			Resource<User> resource = userResourceAssembler.toResource(user);
			return new ResponseEntity<Resource<User>>(resource, HttpStatus.OK);
				
		}catch(Exception e){
			return new ResponseEntity<Resource<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/me", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<User>> updatePrincipal(@RequestBody User user, HttpServletRequest httpServletRequest) {
		try{
			String username = httpServletRequest.getUserPrincipal().getName();
			User userdb = this.userService.findByUsername(username);
			if(userdb == null){
				return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
			}
			if(!userdb.getId().equals(user.getId())){
				return new ResponseEntity<Resource<User>>(HttpStatus.BAD_REQUEST);
			}
			if(!userdb.getVerified()){
				return new ResponseEntity<Resource<User>>(HttpStatus.LOCKED);
			}

			user = this.userService.update(user);
			Resource<User> resource = userResourceAssembler.toResource(user);
			return new ResponseEntity<Resource<User>>(resource, HttpStatus.OK);

		}catch(Exception e){
			return new ResponseEntity<Resource<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/me", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<User>> deletePrincipal(HttpServletRequest httpServletRequest) {
		String username = httpServletRequest.getUserPrincipal().getName();
		User user = this.userService.findByUsername(username);
		if(user == null){
			return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
		}
		user.setDeactivated(true);
		this.userService.update(user);
		return new ResponseEntity<Resource<User>>(HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN"})
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

	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<User>> updateUser(@PathVariable ObjectId id, @RequestBody User user) {
		user.setId(id);

		if(this.userService.findOne(id) == null){
			return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
		}
		user = this.userService.update(user);
		Resource<User> resource = userResourceAssembler.toResource(user);
		return new ResponseEntity<Resource<User>>(resource, HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<User>> deleteUser(@PathVariable ObjectId id) {
		if(this.userService.findOne(id) == null){
			return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
		}
		this.userService.delete(id);
		return new ResponseEntity<Resource<User>>(HttpStatus.OK);
	}
}
