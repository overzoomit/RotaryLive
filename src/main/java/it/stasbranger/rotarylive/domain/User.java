package it.stasbranger.rotarylive.domain;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id
	private ObjectId id;

	private Date creationDate = new Date();
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private ObjectId clubId;

	@NotEmpty
	@Indexed(unique = true)
	private String username;

	@NotEmpty
	private String password;

	private Boolean verified = false;
	
	private Member member;

	private List<Role> roles;

	@Version
	private String version;
	
	public User() {
	}

	public User(User user) {
		this.name = user.getName();
		this.clubId = user.getClubId();
		this.username = user.getUsername();
		this.password = user.getPassword();
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ObjectId getClubId() {
		return clubId;
	}

	public void setClubId(ObjectId clubId) {
		this.clubId = clubId;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

}
