package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientSecretQuestionResponsesResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsPasswordResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.hateoas.UriTemplate;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

        ClientResource clientResource =
                clientResourceAssembler.toResource(((UserClient) user).getClient());

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
                user.isImpersonated(),
                user.getPasswordExpireationDateTime());
        ret.add(linkTo(methodOn(UserClientsResource.class).getById(user.getId())).withSelfRel());
        ret.add(linkTo(methodOn(UserClientsResource.class).authenticated()).withRel("authenticated"));
        ret.add(linkTo(methodOn(UserClientsResource.class).verifyPassword(user.getId())).withRel("verifyPassword"));

        if (!user.isImpersonated()) {
            Link link = linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).secretQuestionResponses(user.getId(), null, null, null)).withRel("secretQuestionResponses");
            UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                    new TemplateVariable("password",
                            TemplateVariable.VariableType.REQUEST_PARAM)));
            ret.add(new Link(uriTemplate, link.getRel()));
            
            ret.add(linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).secretQuestionAsteriskResponse(user.getId(), null)).withRel("secretQuestionAsteriskResponses"));
            ret.add(linkTo(methodOn(UserClientsResource.class).validate(user.getId(), null)).withRel("validate"));
            ret.add(linkTo(methodOn(UserClientsPasswordResource.class).changePassword(null, null, null)).withRel("changePassword"));
        }
        
        return ret;
    }

}
