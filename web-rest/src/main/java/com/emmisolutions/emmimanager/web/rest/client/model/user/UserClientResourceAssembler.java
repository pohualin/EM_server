package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.service.security.SuperUserTeamRoleGenerator;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.client.model.team.TeamResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientSecretQuestionResponsesResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsPasswordResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsResource;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
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

    @Value("${admin.application.entry.point:/admin.html}")
    String adminEntryPoint;

    @Resource
    SuperUserTeamRoleGenerator superUserTeamRoleGenerator;

    Pattern specializedPermissions = Pattern.compile("([A-Z_]*)(_[0-9]+)?(_[0-9]+)");

    public static Link createVerifyPasswordLink(UserClient user) {
        Link verifyPasswordLink = linkTo(methodOn(UserClientsResource.class).verifyPassword(null)).withRel("verifyPassword");
        UriTemplate verifyPasswordUriTemplate = new UriTemplate(verifyPasswordLink.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("password",
                                TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(verifyPasswordUriTemplate, verifyPasswordLink.getRel());
    }

    public static Link updateUserClientLink(UserClient user) {
        Link verifyPasswordLink = linkTo(methodOn(UserClientsResource.class).updateUserClientEmail(user.getId(), null, null, null, null)).withRel("userClientEmail");
        UriTemplate verifyPasswordUriTemplate = new UriTemplate(verifyPasswordLink.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("password",
                                TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(verifyPasswordUriTemplate, verifyPasswordLink.getRel());
    }

    @Override
    public UserClientResource toResource(UserClient user) {
        if (user == null) {
            return null;
        }
        Set<String> perms = new HashSet<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            // strip off the client and team specifics on the authorities
            Matcher matcher = specializedPermissions.matcher(grantedAuthority.getAuthority());
            if (matcher.matches()) {
                perms.add(matcher.replaceAll("$1"));
            } else {
                perms.add(grantedAuthority.getAuthority());
            }
        }

        if (user.isSuperUser()) {
            // give the super user all team permissions (not necessary but nice for the UI)
            user.setTeamRoles(superUserTeamRoleGenerator.allPermissionsOnAllTeams(user.getClient()));
        }

        // add the teams to which this user has access
        Set<TeamResource> teams = new HashSet<>();
        for (UserClientUserClientTeamRole userClientUserClientTeamRole : user.getTeamRoles()) {
            teams.add(roleTeamResourceResourceAssembler.toResource(userClientUserClientTeamRole));
        }

        boolean interruptFlow = false;

        if (user.getNotNowExpirationTime() != null) {
            LocalDateTime dateTime = LocalDateTime.now(DateTimeZone.UTC);
            interruptFlow = dateTime.isBefore(user.getNotNowExpirationTime());
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
                user.isSecretQuestionCreated(),
                clientResourceAssembler.toResource(user.getClient()),
                new ArrayList<>(perms),
                user.isImpersonated(),
                user.getNotNowExpirationTime(),
                user.getPasswordExpireationDateTime(),
                user.getPasswordSavedDateTime(),
                interruptFlow);

        ret.setSecurityQuestionsNotRequiredForReset(user.isSecurityQuestionsNotRequiredForReset());

        ret.add(linkTo(methodOn(UserClientsResource.class).authenticated()).withRel("authenticated"));

        if (!CollectionUtils.isEmpty(teams)) {
            ret.setTeams(teams);
        }

        if (!user.isImpersonated()) {
            // non-impersonation users only
            ret.add(linkTo(methodOn(UserClientsResource.class).getById(user.getId())).withSelfRel());
            ret.add(updateUserClientLink(user));
            ret.add(createVerifyPasswordLink(user));

            Link link = linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).secretQuestionResponses(user.getId(), null, null, null)).withRel("secretQuestionResponses");
            ret.add(new Link(createUriTemplate("password", link), link.getRel()));

            Link updateUserClientSecretQuestion = linkTo(methodOn(UserClientsResource.class).updateUserClient(user.getId(), null)).withRel("updateUserClientSecretQuestionFlag");
            ret.add(new Link(createUriTemplate("secretQuestionsCreated", updateUserClientSecretQuestion), updateUserClientSecretQuestion.getRel()));

            ret.add(linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).secretQuestionAsteriskResponse(user.getId(), null, null)).withRel("secretQuestionAsteriskResponses"));
            ret.add(linkTo(methodOn(UserClientsResource.class).sendValidationEmail(user.getId())).withRel("sendValidationEmail"));
            ret.add(linkTo(methodOn(UserClientsPasswordResource.class).changePassword(null, null, null)).withRel("changePassword"));
            ret.add(linkTo(methodOn(UserClientsResource.class).notNow(user.getId())).withRel("notNow"));
        } else {
            // impersonation users
            ret.add(new Link(
                    UriComponentsBuilder.fromHttpUrl(
                            linkTo(methodOn(UserClientsResource.class).getById(1l)).withSelfRel().getHref())
                            .replacePath(adminEntryPoint)
                            .build(false)
                            .toUriString(), "adminApp"));
        }

        return ret;
    }

    private UriTemplate createUriTemplate(String requestParameterName, Link linkName ){
        return new UriTemplate(linkName.getHref())
        .with(new TemplateVariables(
                new TemplateVariable(requestParameterName,
                        TemplateVariable.VariableType.REQUEST_PARAM)));
    }
    

}
