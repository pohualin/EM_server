package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.service.UserClientService;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Creates a UserClientResource from a UserClient
 */
@Component
public class ClientUserConflictResourceAssembler implements
        ResourceAssembler<List<UserClientService.UserClientConflict>, UserClientResource> {

    @Override
    public UserClientResource toResource(List<UserClientService.UserClientConflict> conflicts) {
        UserClientResource ret = null;
        if (!CollectionUtils.isEmpty(conflicts)) {
            ret = new UserClientResource();
            ret.setConflicts(conflicts);
        }
        return ret;
    }
}
