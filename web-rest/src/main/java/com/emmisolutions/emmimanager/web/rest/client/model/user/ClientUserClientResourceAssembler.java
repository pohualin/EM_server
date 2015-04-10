package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
                entity.isSecretQuestionCreated(),
                clientResourceAssembler.toResource(entity.getClient()),
                null,
                entity.isImpersonated(),
                entity.getNotNowExpirationTime(),
                entity.getPasswordExpireationDateTime(),
                entity.isInterruptLoginFlow());
        ret.add(linkTo(methodOn(UserClientsResource.class).getById(entity.getId())).withSelfRel());
        ret.add(UserClientResourceAssembler.createVerifyPasswordLink(entity));
        ret.add(linkTo(methodOn(UserClientsResource.class).sendValidationEmail(entity.getId())).withRel("sendValidationEmail"));

        return ret;
    }
}
