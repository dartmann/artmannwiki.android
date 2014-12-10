package de.davidartmann.artmannwiki.android.model;

/**
 * 
 * This class stands for an Email Account. E.g. for Gmail or Yahoo.
 *
 */
public class Email extends SoftDeleteEntity {

	private String emailadress;
	
	private String password;
	
	public Email(String e, String p) {
		this.emailadress = e;
		this.password = p;
	}

	public String getEmailadress() {
		return emailadress;
	}

	public void setEmailadress(String emailadress) {
		this.emailadress = emailadress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
