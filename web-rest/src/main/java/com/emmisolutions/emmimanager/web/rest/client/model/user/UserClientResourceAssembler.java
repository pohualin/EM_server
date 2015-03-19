package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.client.model.team.TeamResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientSecretQuestionResponsesResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsPasswordResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsResource;
import org.springframework.hateoas.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserResource from a UserClient
 */
@Component("userClientAuthenticationResourceAssembler")
public class UserClientResourceAssembler implements ResourceAssembler<UserClient, UserClientResource> {

    @Resource(name = "userClientClientResourceAssembler")
    ResourceAssembler<Client, ClientResource> clientResourceAssembler;

    @Resource(name = "userClientUserClientTeamRoleTeamResourceAssembler")
    ResourceAssembler<UserClientUserClientTeamRole, TeamResource> roleTeamResourceResourceAssembler;

    Pattern specializedPermissions = Pattern.compile("([A-Z_]*)_[0-9]+");

    @Override
    public UserClientResource toResource(UserClient user) {
        if (user == null) {
            return null;
        }
        List<String> perms = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            // strip off the client and team specifics on the authorities
            Matcher matcher = specializedPermissions.matcher(grantedAuthority.getAuthority());
            if (matcher.matches()){
                perms.add(matcher.replaceAll("$1"));
            } else {
                perms.add(grantedAuthority.getAuthority());
            }
        }

        // add the teams to which this user has access
        Set<TeamResource> teams = new HashSet<>();
        for (UserClientUserClientTeamRole userClientUserClientTeamRole : user.getTeamRoles()) {
            teams.add(roleTeamResourceResourceAssembler.toResource(userClientUserClientTeamRole));
        }

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
                clientResourceAssembler.toResource(user.getClient()),
                perms,
                user.isImpersonated(),
                user.getPasswordExpireationDateTime(),
                user.getPasswordSavedDateTime());

        ret.add(linkTo(methodOn(UserClientsResource.class).getById(user.getId())).withSelfRel());
        ret.add(linkTo(methodOn(UserClientsResource.class).authenticated()).withRel("authenticated"));
        ret.add(createVerifyPasswordLink(user));

        if (!CollectionUtils.isEmpty(teams)){
            ret.setTeams(teams);
        }

        if (!user.isImpersonated()) {
            ret.add(linkTo(methodOn(UserClientsResource.class).getById(user.getId())).withSelfRel());
            Link link = linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).secretQuestionResponses(user.getId(), null, null, null)).withRel("secretQuestionResponses");
            UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                    new TemplateVariable("password",
                            TemplateVariable.VariableType.REQUEST_PARAM)));
            ret.add(new Link(uriTemplate, link.getRel()));

            ret.add(linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).secretQuestionAsteriskResponse(user.getId(), null)).withRel("secretQuestionAsteriskResponses"));
            ret.add(linkTo(methodOn(UserClientsResource.class).sendValidationEmail(user.getId())).withRel("sendValidationEmail"));
            ret.add(linkTo(methodOn(UserClientsPasswordResource.class).changePassword(null, null, null)).withRel("changePassword"));
        }

        return ret;
    }

    public static Link createVerifyPasswordLink(UserClient user){
        Link verifyPasswordLink = linkTo(methodOn(UserClientsResource.class).verifyPassword(user.getId(), null)).withRel("verifyPassword");
        UriTemplate verifyPasswordUriTemplate = new UriTemplate(verifyPasswordLink.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("password",
                                TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(verifyPasswordUriTemplate, verifyPasswordLink.getRel());
    }

}
