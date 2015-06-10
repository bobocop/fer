package hr.fer.zemris.java.tecaj_14.webforms;

import hr.fer.zemris.java.tecaj_14.model.BlogUser;
import hr.fer.zemris.java.tecaj_14.utility.PasswordChecker;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class UserUnosForm {
	private String nick;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Map<String, String> errors = new HashMap<>();
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public void fill(HttpServletRequest request) {
		nick = request.getParameter("nick");
		firstName = request.getParameter("firstName");
		lastName = request.getParameter("lastName");
		email = request.getParameter("email");
		password = request.getParameter("password");
		checkErrors();
	}
	
	public Set<String> getErrorNames() {
		return errors.keySet();
	}
	
	public String getErrorFor(String errName) {
		return errors.get(errName);
	}
	
	public BlogUser toBlogUser() {
		BlogUser user = new BlogUser();
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setNick(nick);
		user.setPasswordHash(new PasswordChecker().getPasswordHash(password));
		return user;
	}
	
	/**
	 * Checks for errors on the field values.
	 */
	private void checkErrors() {
		if(nick == null 
				|| nick.equals("") 
				|| nick.length() >= 32
				|| nick.contains("/")
				|| nick.contains(";")
				|| nick.contains("=")
				|| nick.contains("?")) {
			nick = "";
			errors.put("nick", "Nick name must not be empty "
					+ "and may contain up to 32 characters except (/, ;, =, ?)");
		}
		if(password == null || password.equals("")) {
			errors.put("password", "Please enter a password!");
		}
		if(email != null && !email.isEmpty()) {
			String[] splitEmail = email.split("@");
			if(splitEmail.length != 2 || splitEmail[1].split("\\.").length < 2) {
				email = "";
				errors.put("email", "The entered e-mail address is invalid");
			}
		}
		if(firstName != null && firstName.length() >= 32) {
			firstName = "";
			errors.put("firstName", "The username may contain up to 32 characters");
		}
		if(lastName != null && lastName.length() >= 64) {
			lastName = "";
			errors.put("lastName", "The last name may contain up to 64 characters");
		}
	}

	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
