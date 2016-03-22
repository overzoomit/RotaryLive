package it.stasbranger.rotarylive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.stasbranger.rotarylive.dao.MemberRepository;
import it.stasbranger.rotarylive.domain.Member;

@Service("MemberService")
public class MemberServiceImpl implements MemberService {
	
	@Autowired private MemberRepository memberRepository;
	
	public void create(Member member){
		memberRepository.save(member);
	}

	public Member update(Member member){
		return memberRepository.save(member);
	}
	
	public void delete(Member member){
		memberRepository.delete(member);
	}
	
	public void delete(String id){
		memberRepository.delete(id);
	}
	
	public Member findOne(String id){
		return memberRepository.findOne(id);
	}
	
	public List<Member> findAll(){
		return memberRepository.findAll();
	}
	
	public Page<Member> findAll(Pageable pageable){
		return memberRepository.findAll(pageable);
	}
}
