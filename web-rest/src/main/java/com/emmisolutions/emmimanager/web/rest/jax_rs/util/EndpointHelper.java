package com.emmisolutions.emmimanager.web.rest.jax_rs.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Common helper methods used by endpoints.
 */
@Component
public class EndpointHelper {

    /**
     * Creates a Pageable.
     *
     * @param pageString     the page number requested
     * @param pageSizeString the number of elements on the page
     * @param sortString     a string in the form of xxx:dir;yyy:dir; where xxx and yyy are property names and dir is one of asc or desc
     * @return a Pageable
     */
    public Pageable createPageable(String pageString, String pageSizeString, String sortString) {
        pageString = StringUtils.stripToNull(pageString);
        pageSizeString = StringUtils.stripToNull(pageSizeString);
        sortString = StringUtils.stripToNull(sortString);
        int page;
        int pageSize;
        try {
            page = Integer.parseInt(pageString);
        } catch (NumberFormatException nfe) {
            page = 0;
        }
        try {
            pageSize = Integer.parseInt(pageSizeString);
        } catch (NumberFormatException nfe) {
            pageSize = 25;
        }

        return new PageRequest(page, pageSize, getSort(sortString));
    }

    private Sort getSort(String sort) {
        List<Sort.Order> orderList = new ArrayList<>();
        for (String part : sort.split(";")) {
            String[] order = part.split(":");
            String property = order[0];
            Sort.Direction direction = null;
            if (order.length == 2) {
                direction = Sort.Direction.fromStringOrNull(order[1]);
            }
            if (direction == null) {
                direction = Sort.Direction.ASC;
            }
            if (StringUtils.isNotBlank(property)) {
                orderList.add(new Sort.Order(direction, property));
            }
        }
        return new Sort(orderList);
    }
}
