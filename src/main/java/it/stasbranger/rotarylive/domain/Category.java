package it.stasbranger.rotarylive.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

public class Category {

	@Id
	private String id;

	private String name;
	
	@Version
	private String version;

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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
