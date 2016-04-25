package it.stasbranger.rotarylive.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.stasbranger.rotarylive.domain.Attach;
import it.stasbranger.rotarylive.resource.AttachResourceAssembler;
import it.stasbranger.rotarylive.service.AttachService;

@RestController
@RequestMapping("/api/attach")
@ExposesResourceFor(Attach.class)
public class AttachController {
	
	@Autowired private AttachService attachService;
	@Autowired private AttachResourceAssembler attachResourceAssembler;
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/{type}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<Attach>> addAttach(@PathVariable("type") String type, @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
		try {
			Attach attach = attachService.createAttach(file, type.toUpperCase());

			Resource<Attach> resource = attachResourceAssembler.toResource(attach);
			return new ResponseEntity<Resource<Attach>>(resource, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Resource<Attach>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
