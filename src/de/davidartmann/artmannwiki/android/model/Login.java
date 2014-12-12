package de.davidartmann.artmannwiki.android.model;

/**
 * 
 * This class stands for a Login Account. E.g. for a Webinterface(FritzBox) or a Windows Account.
 *<br>
 * <u>Attributes: </u>
 * <ul>
 * 	<li>username ({@link String})</li>
 * 	<li>password ({@link String})</li>
 * 	<li>description ({@link String})</li>
 * </ul>
 */
public class Login extends SoftDeleteEntity {

	private String username;
	
	private String password;
	
	private String description;
	
	/**
	 * Empty constructor
	 */
	public Login() {
		
	}

	/**
	 * Constructor to create a new instance with only its own attributes
	 * @param username
	 * @param password
	 * @param description
	 */
	public Login(String username, String password, String description) {
		this.username = username;
		this.password = password;
		this.description = description;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
