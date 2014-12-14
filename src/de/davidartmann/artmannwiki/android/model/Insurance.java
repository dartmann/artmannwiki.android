package de.davidartmann.artmannwiki.android.model;

/**
 * 
 * This class stands for an insurance. E.g. for a social insurance or a health insurance.
 *<br>
 * <u>Attributes: </u>
 * <ul>
 * 	<li>name ({@link String})</li>
 * 	<li>kind ({@link String})</li>
 * 	<li>membershipId ({@link String})</li>
 * </ul>
 */
public class Insurance extends SoftDeleteEntity {

	private String name;
	
	private String kind;
	
	private String membershipId;
	
	/**
	 * Empty constructor
	 */
	public Insurance() {
		
	}
	
	/**
	 * Constructor to create a new instance with only its own attributes
	 * @param name
	 * @param kind
	 * @param membershipId
	 */
	public Insurance(String name, String kind, String membershipId) {
		this.name = name;
		this.kind = kind;
		this.membershipId = membershipId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(String membershipId) {
		this.membershipId = membershipId;
	}
	
	public String toString() {
		return "Name: "+name+"\n"+"Art: "+kind;
	}
	
}
