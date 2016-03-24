package it.stasbranger.rotarylive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.stasbranger.rotarylive.dao.RoleRepository;
import it.stasbranger.rotarylive.domain.Role;

@Service("RoleService")
public class RoleServiceImpl implements RoleService {

	@Autowired private RoleRepository roleRepository;
	
	public void create(Role role){
		roleRepository.save(role);
	}

	public Role update(Role role){
		return roleRepository.save(role);
	}
	
	public void delete(Role role){
		roleRepository.delete(role);
	}
	
	public void delete(String id){
		roleRepository.delete(id);
	}
	
	public Role findOne(String id){
		return roleRepository.findOne(id);
	}
	
	public List<Role> findAll(){
		return roleRepository.findAll();
	}
	
	public Page<Role> findAll(Pageable pageable){
		return roleRepository.findAll(pageable);
	}
	
	public Role findByName(String name){
		return roleRepository.findByName(name);
	}
}
