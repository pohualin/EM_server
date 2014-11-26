package com.emmisolutions.emmimanager.web.rest.model.user.client.team;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.web.rest.resource.GroupsResource;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Reference data for UserClientTeamRole pages
 */
@XmlRootElement(name = "reference-data")
public class UserClientTeamRoleReferenceData extends ResourceSupport {

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private Set<UserClientTeamPermission> userClientTeamPermissions;

    /**
     * Creates a set of blank UserClientPermission objects
     */
    public UserClientTeamRoleReferenceData() {
        userClientTeamPermissions = new HashSet<>();
        for (UserClientTeamPermissionName userClientTeamPermissionName : UserClientTeamPermissionName.values()) {
            userClientTeamPermissions.add(new UserClientTeamPermission(userClientTeamPermissionName));
        }
    }

    /**
     * Link for ref data
     *
     * @return Link reference data for groups and tags
     * @see com.emmisolutions.emmimanager.web.rest.resource.GroupsResource#list(org.springframework.data.domain.Pageable, org.springframework.data.domain.Sort, String, org.springframework.data.web.PagedResourcesAssembler, String...)
     */
    private Link createGroupReferenceDataLink() {
        Link link = linkTo(methodOn(GroupsResource.class).getRefGroups(null, null, null)).withRel("refDataGroups");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
            ));
        return new Link(uriTemplate, link.getRel());
    }
}
