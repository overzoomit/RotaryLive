package it.stasbranger.rotarylive.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.stasbranger.rotarylive.dao.UserRepository;
import it.stasbranger.rotarylive.domain.Attach;
import it.stasbranger.rotarylive.domain.Role;
import it.stasbranger.rotarylive.domain.User;

@Service("UserService")
public class UserServiceImpl implements UserService {

	@Autowired private UserRepository userRepository;
	@Autowired private RoleService roleService;
	@Autowired private AttachService attachService;

	public void create(User user) throws Exception {
		if(userRepository.findByUsername(user.getUsername())!=null) throw new DuplicateKeyException("this account already exists");

		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

		Role role = roleService.findByName("ROLE_USER");
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);	
		user.setRoles(roles);

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

	public User findByUsername(String username){
		return userRepository.findByUsername(username);
	}

	public User addImage(User user) throws IOException {
		MultipartFile image = user.getMember().getFile();

		if(image != null){	
			if(user.getId() == null) user = update(user);
			String type = "IMAGE_PROFILE";
			Attach attach = attachService.createAttach(image, type);

			user.getMember().setUriCode(attach.getUriCode());
			user = userRepository.save(user);
		}
		return user;
	}	
}
