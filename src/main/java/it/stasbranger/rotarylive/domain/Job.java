package it.stasbranger.rotarylive.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Job {

	private String headline;
	
	private String note;
	
	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
