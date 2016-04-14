package it.stasbranger.rotarylive.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Social {

	private String twitter;
	
	private String facebook;
	
	private String linkedin;
	
	private String skype;
	
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getLinkedin() {
		return linkedin;
	}
	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}
	public String getSkype() {
		return skype;
	}
	public void setSkype(String skype) {
		this.skype = skype;
	}
}
