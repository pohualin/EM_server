package com.emmisolutions.emmimanager.web.rest.client.model.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientSecretQuestionResponsesResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsResource;

/**
 * Creates a UserResource from a UserClient
 */
@Component("userClientAuthenticationResourceAssembler")
public class UserClientResourceAssembler implements ResourceAssembler<UserClient, UserClientResource> {

    @Resource(name = "userClientClientResourceAssembler")
    ResourceAssembler<Client, ClientResource> clientResourceAssembler;

    Pattern clientSpecificPermission = Pattern.compile("([A-Z_]*)_[0-9]+");

    @Override
    public UserClientResource toResource(UserClient user) {
        if (user == null) {
            return null;
        }
        List<String> perms = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            Matcher matcher = clientSpecificPermission.matcher(grantedAuthority.getAuthority());
            if (matcher.matches()){
                perms.add(matcher.replaceAll("$1"));
            } else {
                perms.add(grantedAuthority.getAuthority());
            }

        }
        ClientResource clientResource = user instanceof UserClient ? clientResourceAssembler.toResource(((UserClient) user).getClient()) : null;

        UserClientResource ret = new UserClientResource(
                user.getId(),
                user.getVersion(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEmailValidated(),
                clientResource,
                perms,
                user.isImpersonated());
        ret.add(linkTo(methodOn(UserClientsResource.class).getById(user.getId())).withRel("getById"));
        ret.add(linkTo(methodOn(UserClientsResource.class).authenticated()).withSelfRel());
        if (!user.isImpersonated()) {
            ret.add(linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).secretQuestionResponses(user.getId(), null, null)).withRel("secretQuestionResponses"));
            ret.add(linkTo(methodOn(UserClientsResource.class).validate(user.getId(), null)).withRel("validate"));
        }
        
        return ret;
    }

}
