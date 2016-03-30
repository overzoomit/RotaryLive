package it.stasbranger.rotarylive.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Job {

	private String name;
	
	private String note;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
