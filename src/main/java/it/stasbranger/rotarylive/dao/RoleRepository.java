package it.stasbranger.rotarylive.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import it.stasbranger.rotarylive.domain.Role;

public interface RoleRepository extends MongoRepository<Role, ObjectId> {

	public Role findByName(String name);
}