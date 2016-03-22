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

import it.stasbranger.rotarylive.domain.Member;
import it.stasbranger.rotarylive.service.MemberService;

@RestController
@RequestMapping("/api/member")
@ExposesResourceFor(Member.class)
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<Member>> createMember(@RequestBody Member member) {
		this.memberService.create(member);
		return new ResponseEntity<Resources<Member>>(HttpStatus.CREATED);
	}
}
