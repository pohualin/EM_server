package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang3.StringUtils;

/**
 * The search filter for User entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserSearchFilter {

	private String term;

	private StatusFilter status;

	/**
	 * constructor
	 */
	public UserSearchFilter() {
		this.status = StatusFilter.ALL;
	}

	/**
	 * all status plus passed term
	 * 
	 */
	public UserSearchFilter(String term) {
		this(StatusFilter.ALL, term);
	}

	/**
	 * constructor
	 * 
	 * @param status
	 *            to filter
	 * @param term
	 *            to filter
	 */
	public UserSearchFilter(StatusFilter status, String term) {
		this.term = term;
		this.status = status;
	}

	public String getTerm() {
		return term;
	}

	public StatusFilter getStatus() {
		return status;
	}

	/**
	 * Status allowed
	 */
	public enum StatusFilter {
		ALL, ACTIVE_ONLY, INACTIVE_ONLY;

		/**
		 * from string or ACTIVE
		 * 
		 * @param status
		 *            the status string
		 * @return never null, the status or ACTIVE
		 */
		public static StatusFilter fromStringOrActive(String status) {
			if (StringUtils.isNotBlank(status)) {
				for (StatusFilter statusFilter : values()) {
					if (statusFilter.toString().equals(status.toUpperCase())) {
						return statusFilter;
					}
				}
			}
			return ACTIVE_ONLY;
		}
	}

	@Override
	public String toString() {
		return "UserSearchFilter{" + "term=" + term + ", status=" + status
				+ '}';
	}
}
