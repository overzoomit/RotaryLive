package it.stasbranger.rotarylive.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Member;

public interface MemberRepository extends MongoRepository<Member, String> {

	public Member findByUriCode(String uriCode);
}
