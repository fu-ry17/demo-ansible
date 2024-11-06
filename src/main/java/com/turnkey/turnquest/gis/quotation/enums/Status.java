/**
 * 
 */
package com.turnkey.turnquest.gis.quotation.enums;

/**
 * @author Paul Gichure
 *
 */
public enum Status {
	
	ACTIVE("Active", true),
	INACTIVE("Inactive",false);
	
	private String name;
	private boolean isTrue;
	
	/**
	 * 
	 */
	private Status(String name, boolean isTrue) {
		this.name= name;
		this.isTrue = isTrue;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isTrue
	 */
	public boolean isTrue() {
		return isTrue;
	}

	/**
	 * @param isTrue the isTrue to set
	 */
	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}
}
