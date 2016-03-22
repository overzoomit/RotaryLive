package it.stasbranger.rotarylive.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@NotEmpty
	private String name;
	
	private Date creationDate = new Date();

	@JsonIgnore
	private Set<User> users = new HashSet<User>();
	
	@Version
	private String version;

	@Override
	public String getAuthority() {
		return name;
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
