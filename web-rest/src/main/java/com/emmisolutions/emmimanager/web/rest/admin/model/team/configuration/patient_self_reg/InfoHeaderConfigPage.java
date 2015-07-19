package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.InfoHeaderConfigsResource;
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

    public InfoHeaderConfigPage(PagedResources<InfoHeaderConfigResource> infoHeaderConfigResources, Page<InfoHeaderConfig> infoHeaderConfigPage, InfoHeaderConfigSearchFilter filter) {
        pageDefaults(infoHeaderConfigResources, infoHeaderConfigPage);
        addFilterToLinks(filter);
    }

    /**
     * creates a search link for all info header configurations for a given patient self reg config
     * @param patientSelfRegConfig
     * @return Link
     */
    public static Link createFullSearchLink(PatientSelfRegConfig patientSelfRegConfig) {
        Link link = linkTo(methodOn(InfoHeaderConfigsResource.class).listByPatientSelfRegConfig(null, null, patientSelfRegConfig.getId())).withRel("infoHeaderConfigs");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
                ));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(InfoHeaderConfigSearchFilter filter) {
        this.searchFilter = filter;
    }

}
