package it.stasbranger.rotarylive.domain;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Language;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.stasbranger.rotarylive.service.utility.ObjectIdSerializer;

@Document
public class User {

	@Id
	@JsonSerialize(using=ObjectIdSerializer.class)
	private ObjectId id;

	@CreatedDate
	private Date dateCreated = new Date();
	
	@LastModifiedDate
	private Date dateModified = new Date();
	
	@NotEmpty
	@Indexed
	private String name;
	
	@Language
	private String lang;
	
	@NotEmpty
	@DBRef
	private Club club;

	@NotEmpty
	@Indexed(unique = true)
	private String username;

	@NotEmpty
	private String password;

	private Boolean verified = false;
	
	private Boolean deactivated = false;
	
	private Member member;
	
	private Device device;

	private List<Role> roles;

	@Version
	private String version;
	
	public User() {
	}

	public User(User user) {
		this.name = user.getName();
		this.club = user.getClub();
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

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Boolean getDeactivated() {
		return deactivated;
	}

	public void setDeactivated(Boolean deactivated) {
		this.deactivated = deactivated;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
}
