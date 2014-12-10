package de.davidartmann.artmannwiki.android.model;

/**
 * 
 * This class stands for a Login Account. E.g. for a Webinterface(FritzBox) or a Windows Account.
 *
 */
public class Login extends SoftDeleteEntity {

	private String username;
	
	private String password;
	
	private String description;
	
	public Login(String u, String p, String d) {
		this.username = u;
		this.password = p;
		this.description = d;
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
