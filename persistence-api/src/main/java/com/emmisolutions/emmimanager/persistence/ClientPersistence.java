package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Client;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Client persistence class
 */
public interface ClientPersistence {

    Page<Client> list(Pageable page, Set<String> clientNameFilter, StatusFilter status);

    Client save(Client client);

    Client reload(Client client);

    public enum StatusFilter {
        ALL, ACTIVE_ONLY, INACTIVE_ONLY;

        public static StatusFilter fromStringOrAll(String status) {
            if (StringUtils.isNotBlank(status)) {
                for (StatusFilter statusFilter : values()) {
                    if (statusFilter.toString().equals(status.toUpperCase())) {
                        return statusFilter;
                    }
                }
            }
            return ALL;
        }
    }

}
