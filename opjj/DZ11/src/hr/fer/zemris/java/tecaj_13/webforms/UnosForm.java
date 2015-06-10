package hr.fer.zemris.java.tecaj_13.webforms;

import hr.fer.zemris.java.tecaj_13.model.Unos;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Encapsulates the data which is sent by the users through a form.
 */
public class UnosForm {
	private String id;
	private String title;
	private String message;
	private String createdOn;
	private String userEMail;
	private long _id;
	private String _title;
	private String _message;
	private Date _createdOn;
	private String _userEMail;
	private Map<String, String> errors = new HashMap<>();
	private SimpleDateFormat dateFormat = 
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static int MAX_TITLE_LEN = 100;
	public static int MAX_MSG_LEN = 2048;
	
	/**
	 * @return boolean true if there were any errors.
	 */
	public boolean hasError() {
		return !errors.isEmpty();
	}
	
	/**
	 * @param key
	 * @return boolean true if there is an error mapped to the provided 'key'
	 */
	public boolean hasError(String key) {
		return errors.containsKey(key);
	}
	
	public String getErrorFor(String key) {
		return errors.get(key);
	}
	
	public void clearErrorFor(String key) {
		errors.remove(key);
	}
	
	public int getErrorCount() {
		return errors.size();
	}
	
	public void clearErrors() {
		errors.clear();
	}
	
	/**
	 * Scans parameters present in HttpServletRequest and fills the main fields with
	 * provided values.
	 * @param HttpServletRequest request a HTTP request containing the parameters
	 */
	public void fill(HttpServletRequest request) {
		id = request.getParameter("id");
		title = request.getParameter("title");
		message = request.getParameter("message");
		createdOn = request.getParameter("createdOn");
		userEMail = request.getParameter("userEMail");
	}
	
	/**
	 * (copied from the assignment paper)
	 * The method checkErrors() will try to convert main fields into shadow fields. 
	 * Here an errors are possible and they must be handled as described next.
	 * For each shadow field of type String simply copy a value from corresponding 
	 * main field. If corresponding field in domain object has some constraints 
	 * (such as: can not be null or empty, can not be longer than some predefined 
	 * length etc), check if these constraints hold on shadow field. If not,
 	 * add appropriate error in errors map using main field property name as key.
	 * For each shadow field of other type than String try to perform a conversion;
	 * if successful, and if corresponding field in domain object has some 
	 * constraints (such as: can not be null or empty, can not
  	 * be longer than some predefined length etc), check if these constraints 
  	 * hold on shadow field. If conversion fails or there are unsatisfied constraints, 
  	 * add appropriate error in errors map using main
	 * field property name as key.
	 * @return boolean true if at least one error occurred. False otherwise.
	 */
	public boolean checkErrors() {
		_title = title;
		if(title == null 
				|| title.length() > MAX_TITLE_LEN 
				|| title.length() == 0) {
			errors.put("title", "The title cannot be an empty string or"
					+ " longer than 100 characters");
		}
		_message = message;
		if(message == null 
				|| message.length() > MAX_MSG_LEN
				|| message.length() == 0) {
			errors.put("message", "The message cannot be an empty string "
					+ "or longer than 2048 characters");
		}
		_userEMail = userEMail;
		if(userEMail == null || !userEMail.contains("@")) {
			errors.put("userEMail", "The entered e-mail address is invalid");
		}
		try {
			_id = Long.parseLong(id);
		} catch (NumberFormatException e) {
			errors.put("id", "Invalid ID number");
		}
		try {
			_createdOn = dateFormat.parse(createdOn);
		} catch (ParseException e) {
			errors.put("createdOn", "Invalid date value");
		}
		return errors.isEmpty();	// returns true if some error occured
	}
	
	/**
	 * Fills the shadow fields based on content found in given Unos instance and then it
	 * fills the main fields (String-typed shadow fields will be simply copied, 
	 * non-String-typed shadow fields will be converted into String representation).
	 * @param unos to get the data from
	 */
	public void fromDomainObject(Unos unos) {
		_id = unos.getId();
		_title = unos.getTitle();
		_message = unos.getMessage();
		_userEMail = unos.getUserEMail();
		_createdOn = unos.getCreatedOn();
		if(_createdOn == null) {
			_createdOn = new Date();
		}
		id = (_id == 0) ? null : Long.valueOf(_id).toString();
		title = _title;
		message = _message;
		userEMail = _userEMail;
		createdOn = dateFormat.format(_createdOn);
	}
	
	/**
	 * Fills the given Unos instance with values found in shadow fields.
	 * @param unos to be filled
	 */
	public void toDomainObject(Unos unos) {
		unos.setId(_id);
		unos.setTitle(_title);
		unos.setMessage(_message);
		unos.setUserEMail(_userEMail);
		unos.setCreatedOn(_createdOn);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUserEMail() {
		return userEMail;
	}

	public void setUserEMail(String userEMail) {
		this.userEMail = userEMail;
	}
}
