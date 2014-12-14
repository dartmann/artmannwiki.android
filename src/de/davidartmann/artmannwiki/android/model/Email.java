package de.davidartmann.artmannwiki.android.model;

/**
 * 
 * This class stands for an Email Account. E.g. for Gmail or Yahoo.
 *<br>
 * <u>Attributes: </u>
 * <ul>
 * 	<li>emailadress ({@link String})</li>
 * 	<li>password ({@link String})</li>
 * </ul>
 */
public class Email extends SoftDeleteEntity {

	private String emailaddress;
	
	private String password;
	
	/**
	 * Empty constructor
	 */
	public Email() {
		
	}
	
	/**
	 * Constructor to create a new instance with only its own attributes
	 * @param emailaddress
	 * @param password
	 */
	public Email(String emailaddress, String password) {
		this.emailaddress = emailaddress;
		this.password = password;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString() {
		return "Emailadresse: "+emailaddress;
	}
	
}
