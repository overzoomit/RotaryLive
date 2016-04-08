package it.stasbranger.rotarylive.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.stasbranger.rotarylive.domain.Role;

public interface RoleService {

	public void create(Role role);
	public Role update(Role role);
	public Role findOne(ObjectId id);
	public List<Role> findAll();
	public Page<Role> findAll(Pageable pageable);
	public void delete(ObjectId id);
	public void delete(Role role);
	public Role findByName(String name);
}
