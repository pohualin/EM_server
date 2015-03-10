package com.emmisolutions.emmimanager.web.rest.client.model.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.annotation.Resource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsResource;

/**
 * Creates a UserClientResource from a UserClient
 */
@Component
public class ClientUserClientResourceAssembler implements
        ResourceAssembler<UserClient, UserClientResource> {
    @Resource(name = "userClientClientResourceAssembler")
    ResourceAssembler<Client, ClientResource> clientResourceAssembler;

    @Override
    public UserClientResource toResource(UserClient entity) {
        if (entity == null) {
            return null;
        }
        ClientResource clientResource = entity instanceof UserClient ? clientResourceAssembler
                .toResource(((UserClient) entity).getClient()) : null;

        UserClientResource ret = new UserClientResource(
                entity.getId(),
                entity.getVersion(),
                entity.getLogin(), 
                entity.getFirstName(),
                entity.getLastName(), 
                entity.getEmail(), 
                entity.isActive(),
                entity.isAccountNonExpired(), 
                entity.isAccountNonLocked(),
                entity.isCredentialsNonExpired(),
                entity.isEmailValidated(),
                clientResource, null, entity.isImpersonated(), entity.getPasswordExpireationDateTime());
        ret.add(linkTo(methodOn(UserClientsResource.class).getById(entity.getId())).withSelfRel());
        return ret;
    }
}
