package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

/**
 * Encapsulates the data which is sent to the persistent storage. Unlike UnosForm,
 * its fields are of the same type the actual data is.
 */
public class Unos {
	private long id;
	private String title;
	private String message;
	private Date createdOn;
	private String userEMail;
	
	public Unos() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getUserEMail() {
		return userEMail;
	}

	public void setUserEMail(String userEMail) {
		this.userEMail = userEMail;
	}

	@Override
	public String toString() {
		return "Unos id="+id;
	}
}
