package com.emmisolutions.emmimanager.persistence.impl.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * This class handle all related to matching criteria in the search popups or search text boxs
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
    public String normalizeName(String name){
    	String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
    	if (StringUtils.isNotBlank(normalizedName)){
    		normalizedName = normalizedName.replaceAll("[^a-z0-9 ]*", "");
    	}
        if (StringUtils.isBlank(normalizedName)){
            normalizedName = name;
        }
    	return normalizedName;
    }
}
