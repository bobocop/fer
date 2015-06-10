package zemris.fer.hr.models;

/**
 * Model of the data that the application keeps track of.
 * It represents information that the phone user may wish
 * to keep about their contatcs.
 * @author karlo
 *
 */
public class Contact {
	private String name;
	private String phoneNum;
	private String email;
	private String imgFile;
	private String note;
	private String fbProfile;
	
	public Contact(String name, String phoneNum, String email, String imgFile,
			String note, String fbProfile) {
		this.name = name;
		this.phoneNum = phoneNum;
		this.email = email;
		this.imgFile = imgFile;
		this.note = note;
		this.fbProfile = fbProfile;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImgFile() {
		return imgFile;
	}
	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getFbProfile() {
		return fbProfile;
	}
	public void setFbProfile(String fbProfile) {
		this.fbProfile = fbProfile;
	}
}
