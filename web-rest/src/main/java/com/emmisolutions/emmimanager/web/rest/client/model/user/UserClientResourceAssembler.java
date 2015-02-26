package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserResource from a UserClient
 */
@Component("userClientAuthenticationResourceAssembler")
public class UserClientResourceAssembler implements ResourceAssembler<User, UserClientResource> {

    @Resource(name = "userClientClientResourceAssembler")
    ResourceAssembler<Client, ClientResource> clientResourceAssembler;

    @Override
    public UserClientResource toResource(User user) {
        if (user == null) {
            return null;
        }
        List<String> perms = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            perms.add(grantedAuthority.getAuthority());
        }
        ClientResource clientResource = user instanceof UserClient ?
                clientResourceAssembler.toResource(((UserClient) user).getClient()) : null;

        UserClientResource ret = new UserClientResource(
                user.getId(),
                user.getVersion(),
                user instanceof UserClient ? ((UserClient) user).getLogin() : ((UserAdmin) user).getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user instanceof UserClient ? ((UserClient) user).getEmail() : ((UserAdmin) user).getEmail(),
                user.isActive(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEmailValidated(),
                clientResource,
                perms);
        ret.add(linkTo(methodOn(UserClientsResource.class).authenticated()).withSelfRel());
        ret.add(linkTo(methodOn(UserClientsResource.class).validate(user.getId(), null)).withRel("validate"));
        return ret;
    }

}
