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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.stasbranger.rotarylive.domain.Club;
import it.stasbranger.rotarylive.resource.ClubResource;
import it.stasbranger.rotarylive.resource.ClubResourceAssembler;
import it.stasbranger.rotarylive.service.ClubService;

@RestController
@RequestMapping("/api/club")
@ExposesResourceFor(Club.class)
public class ClubController {


	@Autowired
	private ClubService clubService;
	@Autowired
	private ClubResourceAssembler clubResourceAssembler;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public HttpEntity<PagedResources<ClubResource>> showClubs(@RequestParam(value = "q", required = false) String query, @PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC, sort = "name") Pageable pageable, PagedResourcesAssembler<Club> assembler) {
		Page<Club> clubs = this.clubService.findByNameContainingIgnoreCase(query, pageable);
		PagedResources<ClubResource> resources = assembler.toResource(clubs, clubResourceAssembler);
		return new ResponseEntity<PagedResources<ClubResource>>(resources, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<Club>> showClub(@PathVariable ObjectId id) {
		try{
			Club club = this.clubService.findOne(id);
			if(club == null){
				return new ResponseEntity<Resource<Club>>(HttpStatus.NOT_FOUND);
			}
			Resource<Club> resource = clubResourceAssembler.toResource(club);
			return new ResponseEntity<Resource<Club>>(resource, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<Resource<Club>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public HttpEntity<Resources<Club>> createClub(@RequestBody Club club) {
		try{
			this.clubService.create(club);
			return new ResponseEntity<Resources<Club>>(HttpStatus.CREATED);
		}catch(DuplicateKeyException e){
			return new ResponseEntity<Resources<Club>>(HttpStatus.CONFLICT);	
		}catch(Exception e){
			return new ResponseEntity<Resources<Club>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<Club>> updateClub(@PathVariable ObjectId id, @RequestBody Club club) {
		club.setId(id);

		if(this.clubService.findOne(id) == null){
			return new ResponseEntity<Resource<Club>>(HttpStatus.NOT_FOUND);
		}
		club = this.clubService.update(club);
		Resource<Club> resource = clubResourceAssembler.toResource(club);
		return new ResponseEntity<Resource<Club>>(resource, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<Club>> deleteClub(@PathVariable ObjectId id) {
		if(this.clubService.findOne(id) == null){
			return new ResponseEntity<Resource<Club>>(HttpStatus.NOT_FOUND);
		}
		this.clubService.delete(id);
		return new ResponseEntity<Resource<Club>>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<Club>> imageClub(@PathVariable("id") ObjectId id, @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
		try {
			Club club = clubService.findOne(id);
			if (club == null) {
				return new ResponseEntity<Resource<Club>>(HttpStatus.NOT_FOUND);
			}
			club.setFile(file);
			club = clubService.addImage(club);
			Resource<Club> resource = clubResourceAssembler.toResource(club);
			return new ResponseEntity<Resource<Club>>(resource, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Resource<Club>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
