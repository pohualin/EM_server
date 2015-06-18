package com.emmisolutions.emmimanager.web.rest.admin.model.user;

import com.emmisolutions.emmimanager.model.user.admin.*;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.groups.ReferenceGroupPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.location.LocationPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.patient.AdminPatientResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.schedule.ScheduledProgramResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.UserClientPage;
import com.emmisolutions.emmimanager.web.rest.admin.resource.*;
import org.springframework.hateoas.*;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName.PERM_ADMIN_SUPER_USER;
import static com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName.PERM_GOD;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


/**
 * Creates a UserResource from a User
 */
@Component
public class UserResourceAssembler implements ResourceAssembler<UserAdmin, UserResource> {

    private static final MappingDiscoverer discoverer = new AnnotationMappingDiscoverer(RequestMapping.class);

    @Override
    public UserResource toResource(UserAdmin user) {
        List<UserAdminPermissionName> perms = new ArrayList<>();
        Set<UserAdminRole> roles = new HashSet<>();
        for (UserAdminUserAdminRole role : user.getRoles()) {
            roles.add(role.getUserAdminRole());
            for (UserAdminPermission permission : role.getUserAdminRole().getPermissions()) {
                perms.add(permission.getName());
            }
        }
        UserResource ret = new UserResource(
                user.getId(),
                user.getVersion(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                perms, roles);
        ret.add(linkTo(methodOn(UsersResource.class).authenticated()).withSelfRel());
        ret.add(ClientPage.createFullSearchLink());
        ret.add(createClientByIdLink());
        ret.add(createProviderByIdLink());
        ret.add(createLocationByIdLink());
        ret.add(createUserClientByIdLink());
        ret.add(ClientPage.createReferenceDataLink());
        ret.add(LocationPage.createFullSearchLink());
        ret.add(LocationPage.createReferenceDataLink());
        ret.add(ReferenceGroupPage.createGroupReferenceDataLink());
        ret.add(ReferenceGroupPage.createActiveReferenceGroupsDataLink());
        ret.add(TeamPage.createFullSearchLink());
        ret.add(linkTo(methodOn(TeamsResource.class).getReferenceData()).withRel("teamsReferenceData"));
        ret.add(ProviderPage.createProviderFullSearchLink());
        ret.add(linkTo(methodOn(ProvidersResource.class).getReferenceData()).withRel("providersReferenceData"));
        ret.add(UserPage.createFullSearchLink());
        ret.add(UserAdminRolePage.createUserAdminRolesLink());
        ret.add(createUserByIdLink());
        ret.add(UserClientPage.createFullSearchLink());
        ret.add(linkTo(methodOn(UserClientsResource.class).getReferenceData()).withRel("userClientReferenceData"));
        ret.add(AdminPatientResourcePage.searchLink());
        ret.add(createPatientByIdLink());
        ret.add(ScheduledProgramResourcePage.searchLink());
        if (perms.contains(PERM_GOD) || perms.contains(PERM_ADMIN_SUPER_USER)) {
            ret.add(referenceTagsLinkForAdmin());
        }

        if (user.isWebApiUser()) {
            // add the password link when the user is a web api user
            ret.setWebApiUser(user.isWebApiUser());
            ret.add(linkTo(methodOn(UsersResource.class).updatePassword(user.getId(), null)).withRel("password"));
        }

        return ret;
    }

    /**
     * Creates link for GET of reference tags for admin functions
     *
     * @return the link
     */
    public Link referenceTagsLinkForAdmin() {
        Link link = linkTo(methodOn(AdminFunctionsResource.class).getRefData(null, null)).withRel("referenceTags");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", REQUEST_PARAM),
                        new TemplateVariable("size", REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", REQUEST_PARAM_CONTINUED)
                ));
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * Load clients by id link
     *
     * @return the link
     */
    public Link createClientByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(ClientsResource.class).get(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("clientById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(ClientsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(ClientsResource.class, method),
                    link.getRel());
        }
        return null;
    }

    /**
     * Load providers by id
     *
     * @return the link
     */
    public Link createProviderByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations =
                (DummyInvocationUtils.LastInvocationAware) methodOn(ProvidersResource.class).getById(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("providerById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(ProvidersResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(ProvidersResource.class, method),
                    link.getRel());
        }
        return null;
    }

    /**
     * Load locations by id
     *
     * @return the link
     */
    public Link createLocationByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations =
                (DummyInvocationUtils.LastInvocationAware) methodOn(LocationsResource.class).get(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("locationById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(LocationsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(LocationsResource.class, method),
                    link.getRel());
        }
        return null;
    }

    /**
     * Load UserClient by id
     *
     * @return the link
     */
    public Link createUserClientByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations =
                (DummyInvocationUtils.LastInvocationAware) methodOn(UserClientsResource.class).get(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("userClientById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(UserClientsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(UserClientsResource.class, method),
                    link.getRel());
        }
        return null;
    }

    /**
     * Load user by id
     *
     * @return the link
     */
    public Link createUserByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations =
                (DummyInvocationUtils.LastInvocationAware) methodOn(UsersResource.class).get(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("userById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(UsersResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(UsersResource.class, method),
                    link.getRel());
        }
        return null;
    }

    /**
     * Load patient by id
     *
     * @return the link
     */
    public Link createPatientByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations =
                (DummyInvocationUtils.LastInvocationAware) methodOn(AdminPatientsResource.class).get(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("patientById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(AdminPatientsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(AdminPatientsResource.class, method),
                    link.getRel());
        }
        return null;
    }

}
