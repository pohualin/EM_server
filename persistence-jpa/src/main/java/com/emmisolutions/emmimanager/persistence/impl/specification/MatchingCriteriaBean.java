package com.emmisolutions.emmimanager.persistence.impl.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * This class handle all related to matching criteria in the search popups or
 * search text boxs
 *
 * @author claudiodesalvo
 *
 */

@Component
public class MatchingCriteriaBean {

    public MatchingCriteriaBean() {

    }

    /**
     * Remove all special characters to the name
     *
     * @param name
     * @return
     */
    public String normalizeName(String name) {
	String normalizedName = StringUtils.trimToEmpty(StringUtils
		.lowerCase(name));
	if (StringUtils.isNotBlank(normalizedName)) {
	    normalizedName = normalizedName.replaceAll("[^a-z0-9 ]*", "");
	}
	if (StringUtils.isBlank(normalizedName)) {
	    normalizedName = name;
	}
	return normalizedName;
    }

    /**
     * Normalize a String... of names to be used to save in normalizedName
     * column
     * 
     * @param names
     *            to be normalized
     * @return normalized name to be used
     */
    public String normalizedName(String... names) {
	StringBuilder sb = new StringBuilder();
	for (String name : names) {
	    String normalized = StringUtils.trimToEmpty(StringUtils
		    .lowerCase(name));
	    if (StringUtils.isNotBlank(normalized)) {
		normalized = normalized.replaceAll("[^a-z0-9]*", "");
		sb.append(normalized);
	    }
	}
	return sb.toString();
    }
}
