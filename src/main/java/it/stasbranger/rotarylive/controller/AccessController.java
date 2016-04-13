package it.stasbranger.rotarylive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.stasbranger.rotarylive.domain.Club;
import it.stasbranger.rotarylive.domain.User;
import it.stasbranger.rotarylive.resource.ClubResource;
import it.stasbranger.rotarylive.resource.ClubResourceAssembler;
import it.stasbranger.rotarylive.service.ClubService;
import it.stasbranger.rotarylive.service.UserService;

@RestController
@ExposesResourceFor(User.class)
public class AccessController {

	@Autowired private UserService userService;
	@Autowired
	private ClubService clubService;
	@Autowired
	private ClubResourceAssembler clubResourceAssembler;
	
	@RequestMapping(value="/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	@RequestMapping(value="/club", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public HttpEntity<PagedResources<ClubResource>> showClubs(@RequestParam(value = "q", required = false) String query, @PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC, sort = "name") Pageable pageable, PagedResourcesAssembler<Club> assembler) {
		Page<Club> clubs = this.clubService.findByNameContainingIgnoreCase(query, pageable);
		PagedResources<ClubResource> resources = assembler.toResource(clubs, clubResourceAssembler);
		return new ResponseEntity<PagedResources<ClubResource>>(resources, HttpStatus.OK);
	}
}
