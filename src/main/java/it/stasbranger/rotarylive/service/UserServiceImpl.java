package it.stasbranger.rotarylive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.stasbranger.rotarylive.dao.UserRepository;
import it.stasbranger.rotarylive.domain.User;

@Service("UserService")
public class UserServiceImpl implements UserService {

	@Autowired private UserRepository userRepository;
	
	public void create(User user){
		userRepository.save(user);
	}
	
	public User update(User user){
		return userRepository.save(user);
	}
	
	public void delete(User user){
		userRepository.delete(user);
	}
	
	public void delete(String id){
		userRepository.delete(id);
	}
	
	public User findOne(String id){
		return userRepository.findOne(id);
	}
	
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	public Page<User> findAll(Pageable pageable){
		return userRepository.findAll(pageable);
	}
}
