package de.davidartmann.artmannwiki.android.model;

import java.io.Serializable;

/**
 * 
 * This class stands for a Bank Account. E.g. for Sparkasse or DiBa.
 * <br>
 * <u>Attributes: </u>
 * <ul>
 * 	<li>owner ({@link String})</li>
 * 	<li>iban ({@link String})</li>
 * 	<li>bic ({@link String})</li>
 * 	<li>pin ({@link String})</li>
 * </ul>
 *
 */
public class Account extends SoftDeleteEntity implements Serializable{

	private static final long serialVersionUID = 6224508889742851291L;

	private String owner;
	
	private String iban;
	
	private String bic;
	
	private String pin;
	
	/**
	 * Empty constructor
	 */
	public Account() {
		
	}
	
	/**
	 * Constructor to create a new instance with only its own attributes
	 * @param owner
	 * @param iban
	 * @param bic
	 * @param pin
	 */
	public Account(String owner, String iban, String bic, String pin) {
		this.owner = owner;
		this.iban = iban;
		this.bic = bic;
		this.pin = pin;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	public String toString() {
		return "Eigentümer: " + owner+"\n"+"IBAN: " + iban;
	}
	
	public String fullString() {
		return "Eigentümer: " + owner+"\n"+"IBAN: " + iban+"\n"+"BIC: "+bic;
	}
}
