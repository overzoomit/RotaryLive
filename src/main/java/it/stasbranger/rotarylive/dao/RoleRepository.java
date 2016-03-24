package it.stasbranger.rotarylive.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

	public Role findByName(String name);
}