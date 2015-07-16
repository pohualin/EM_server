package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.GroupsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of GroupResource objects.
 */
@XmlRootElement(name = "info-header-config-page")
public class InfoHeaderConfigPage extends PagedResource<InfoHeaderConfigResource> {

    @XmlElement(name = "filter")
    private InfoHeaderConfigSearchFilter searchFilter;

    public InfoHeaderConfigPage() {
    }

    public InfoHeaderConfigPage(PagedResources<InfoHeaderConfigResource> infoHeaderConfigResources, Page<InfoHeaderConfig> infoHeaderConfigPage, InfoHeaderConfigSearchFilter filter) {
        pageDefaults(infoHeaderConfigResources, infoHeaderConfigPage);
        addFilterToLinks(filter);
    }
/*

    */
/**
     * Create the search link
     *
     * @param client to use
     * @return Link for group searches
     * @see GroupsResource#listGroupsByClientID(org.springframework.data.domain.Pageable, org.springframework.data.web.PagedResourcesAssembler, Long)
     *//*

    public static Link createFullSearchLink(Client client) {
        Link link = linkTo(methodOn(GroupsResource.class).listGroupsByClientID(null, null, client.getId())).withRel("groups");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
                        ));
        return new Link(uriTemplate, link.getRel());
    }
*/


    private void addFilterToLinks(InfoHeaderConfigSearchFilter filter) {
        this.searchFilter = filter;
    }

}
