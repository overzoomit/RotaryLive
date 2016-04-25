package it.stasbranger.rotarylive.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Device {

	private String os;
	private String id;
	
	public Device() {
		super();
	}
	public Device(String os, String id) {
		super();
		this.os = os;
		this.id = id;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
