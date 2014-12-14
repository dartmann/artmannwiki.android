package de.davidartmann.artmannwiki.android.model;

/**
 * 
 * This class stands for an everything what needs not more than an text and an appropriate description.
 *<br>
 * <u>Attributes: </u>
 * <ul>
 * 	<li>text ({@link String})</li>
 * 	<li>description ({@link String})</li>
 * </ul>
 */
public class Miscellaneous extends SoftDeleteEntity {

	private String text;
	
	private String description;
	
	/**
	 * Empty constructor
	 */
	public Miscellaneous() {
		
	}
	
	/**
	 * Constructor to create a new instance with only its own attributes
	 * @param text
	 * @param description
	 */
	public Miscellaneous(String text, String description) {
		this.text = text;
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		return "Text: "+text+"\n"+"Beschreibung: "+description;
	}
	
}
