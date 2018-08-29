/**
 *
 */
package com.ajax;

import java.io.Serializable;

/**
 * @author USER
 *
 */
public class JsonSelectItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String value;
	private String desc;
	private boolean selected;

	/**
	 *
	 */
	public JsonSelectItem() {
		super();
	}

	/**
	 *
	 * @param value
	 * @param desc
	 * @param selected
	 */
	public JsonSelectItem(String value, String desc, boolean selected) {
		super();
		this.value = value;
		this.desc = desc;
		this.selected = selected;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
