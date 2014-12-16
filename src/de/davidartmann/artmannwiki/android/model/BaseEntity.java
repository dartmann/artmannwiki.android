package de.davidartmann.artmannwiki.android.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * This class is the simplest entity class and holds attributes for the primary keys(id), 
 * checkings of when the data was first stored(createTime) and when it got updated the last time(lastUpdate)
 * <br>
 * <u>Attributes: </u>
 * <ul>
 * 	<li>id ({@link String})</li>
 * 	<li>lastUpdate ({@link Date})</li>
 * 	<li>createTime ({@link Date})</li>
 * </ul>
 */
public class BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 5812478337945783960L;

	private long id;
	
	private Date lastUpdate;
	
	private Date createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
