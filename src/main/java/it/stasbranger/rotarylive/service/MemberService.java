package it.stasbranger.rotarylive.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.stasbranger.rotarylive.domain.Member;

public interface MemberService {

	public void create(Member member);
	public Member update(Member member);
	public Member findOne(String id);
	public List<Member> findAll();
	public Page<Member> findAll(Pageable pageable);
	public void delete(String id);
	public void delete(Member member);
}
