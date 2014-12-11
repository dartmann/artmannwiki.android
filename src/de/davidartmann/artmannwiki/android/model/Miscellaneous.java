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
	
	public Miscellaneous(String t, String d) {
		this.text = t;
		this.description = d;
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
	
}
