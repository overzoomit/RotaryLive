package it.stasbranger.rotarylive.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.stasbranger.rotarylive.service.utility.ObjectIdSerializer;

@Document
public class Club {

	@Id
	@JsonSerialize(using=ObjectIdSerializer.class)
	private ObjectId id;

    @Indexed(unique = true)
	private String name;
	
	private Address address;
	
	private String email;
	
	private String phone;
	
	private String website;
	
	@JsonSerialize(using=ObjectIdSerializer.class)
	private ObjectId logoId;
	
	@JsonIgnore
    @Transient
    private MultipartFile file;

	@Version
	private String version;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ObjectId getLogoId() {
		return logoId;
	}

	public void setLogoId(ObjectId logoId) {
		this.logoId = logoId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
}
