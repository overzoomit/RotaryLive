package it.stasbranger.rotarylive.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.stasbranger.rotarylive.service.utility.ObjectIdSerializer;

@Document
public class Event {

	@Id
	@JsonSerialize(using=ObjectIdSerializer.class)
	private ObjectId id;

	@NotEmpty
	private String name;
	
	private Date startDate;
	
	private Date endDate;
	
	private Boolean allDay = false;
	
	private String text;
	
	private String type;
	
	@JsonSerialize(using=ObjectIdSerializer.class)
	private ObjectId imageId;
	
	@JsonIgnore
    @Transient
    private MultipartFile file;

	@CreatedDate
	private Date dateCreated = new Date();
	
	@LastModifiedDate
	private Date dateModified = new Date();
	
	@Transient
	private Boolean booked = false;
	
	List<Booking> booking = new ArrayList<Booking>();
	
	@DBRef
	private Location location;

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getAllDay() {
		return allDay;
	}

	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public List<Booking> getBooking() {
		return booking;
	}

	public void setBooking(List<Booking> booking) {
		this.booking = booking;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ObjectId getImageId() {
		return imageId;
	}

	public void setImageId(ObjectId imageId) {
		this.imageId = imageId;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Boolean getBooked() {
		return booked;
	}

	public void setBooked(Boolean booked) {
		this.booked = booked;
	}
}
