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
	
	public Insurance(String n, String k, String m) {
		this.name = n;
		this.kind = k;
		this.membershipId = m;
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
	
}
