package de.davidartmann.artmannwiki.android.model;

import java.io.Serializable;

/**
 * 
 * This class is the is for entities which should be able to get soft deleted.
 * Which simply means, to turn their active ({@link Boolean}) attribute to false.
 * <br>
 * <u>Attributes: </u>
 * <ul>
 * 	<li>active ({@link Boolean})</li>
 * </ul>
 */
public class SoftDeleteEntity extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 3504378201032484301L;
	
	private boolean active;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
