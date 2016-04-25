package it.stasbranger.rotarylive.domain;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Booking {

	@DBRef
	private User user;
	
	private String comment;
	
	private Date date = new Date();
		
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
