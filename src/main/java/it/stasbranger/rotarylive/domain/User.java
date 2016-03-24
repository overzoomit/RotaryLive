package it.stasbranger.rotarylive.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

	@Id
	private String id;

	@NotEmpty
	private String name;
	
	@NotEmpty
	private String clubName;

	@NotEmpty
	private String login;

	@NotEmpty
	private String password;

	@Version
	private String version;

	@DBRef
	private Member member;
	
	@JsonIgnore
	private Set<Role> roles = new HashSet<Role>();

	public User() {
	}

	public User(User user) {
		this.name = user.getName();
		this.clubName = user.getClubName();
		this.login = user.getLogin();
		this.password = user.getPassword();
		this.roles = user.getRoles();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
