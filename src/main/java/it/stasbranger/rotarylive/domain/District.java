package it.stasbranger.rotarylive.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.stasbranger.rotarylive.service.utility.ObjectIdSerializer;

@Document
public class District {

	@Id
	@JsonSerialize(using=ObjectIdSerializer.class)
	private ObjectId id;
	
	private String name;
	
	private Club club;
	
	@Version
	private String version;

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

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
