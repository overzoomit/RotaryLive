package it.stasbranger.rotarylive.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.stasbranger.rotarylive.domain.User;

public interface UserService {
	
	public void create(User user) throws Exception;
	public User update(User user);
	public User findOne(String id);
	public List<User> findAll();
	public Page<User> findAll(Pageable pageable);
	public void delete(String id);
	public void delete(User user);
}
