package it.stasbranger.rotarylive.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
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

import it.stasbranger.rotarylive.domain.Member;
import it.stasbranger.rotarylive.domain.User;
import it.stasbranger.rotarylive.resource.UserResourceAssembler;
import it.stasbranger.rotarylive.service.MemberService;
import it.stasbranger.rotarylive.service.UserService;

@RestController
@RequestMapping("/api/member")
@ExposesResourceFor(Member.class)
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserResourceAssembler userResourceAssembler;
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<Member>> createMember(@RequestBody Member member) {
		this.memberService.create(member);
		return new ResponseEntity<Resources<Member>>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<Resource<User>> imageUser(@PathVariable("id") String id, @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
        try {
            String username = httpServletRequest.getUserPrincipal().getName();
            User user = userService.findOne(id);
            if (user == null) {
            	return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
            }
            if (!username.equals(user.getLogin()) && !httpServletRequest.isUserInRole("ADMIN")) {
            	return new ResponseEntity<Resource<User>>(HttpStatus.METHOD_NOT_ALLOWED);
            }
            Member member = user.getMember();
            if(member == null) member = new Member();
            member.setFile(file);
            user = userService.addImage(user);
            Resource<User> resource = userResourceAssembler.toResource(user);
    		return new ResponseEntity<Resource<User>>(resource, HttpStatus.OK);
        } catch (Exception e) {
        	return new ResponseEntity<Resource<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
