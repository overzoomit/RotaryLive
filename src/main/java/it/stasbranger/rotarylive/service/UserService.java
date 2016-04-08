package it.stasbranger.rotarylive.service;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.stasbranger.rotarylive.domain.User;

public interface UserService {
	
	public void create(User user) throws Exception;
	public User update(User user);
	public User findOne(ObjectId id);
	public List<User> findAll();
	public Page<User> findAll(Pageable pageable);
	public void delete(ObjectId id);
	public void delete(User user);
	public User findByUsername(String username);
	public User addImage(User user) throws IOException;
}
