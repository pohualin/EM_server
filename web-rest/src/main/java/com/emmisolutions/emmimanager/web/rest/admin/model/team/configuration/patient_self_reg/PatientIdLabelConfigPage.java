package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.PatientIdLabelConfigsResource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


/**
 * A HATEOAS wrapper for a page of PatientIdLabelConfigResource objects.
 */
@XmlRootElement(name = "patient-id-label-config-page")
public class PatientIdLabelConfigPage extends PagedResource<PatientIdLabelConfigResource> {

    @XmlElement(name = "filter")
    private PatientIdLabelConfigSearchFilter searchFilter;

    public PatientIdLabelConfigPage(PagedResources<PatientIdLabelConfigResource> patientIdLabelConfigResources, Page<PatientIdLabelConfig> patientIdLabelConfigs, PatientIdLabelConfigSearchFilter filter) {
        pageDefaults(patientIdLabelConfigResources, patientIdLabelConfigs);
        addFilterToLinks(filter);
    }

    /**
     * creates a search link for all patient id label configurations for a given patient self reg config
     * @param patientSelfRegConfig
     * @return Link
     */
    public static Link createFullSearchLink(PatientSelfRegConfig patientSelfRegConfig) {
        Link link = linkTo(methodOn(PatientIdLabelConfigsResource.class).listByPatientSelfRegConfig(null, null, patientSelfRegConfig.getId())).withRel("patientIdLabelConfig");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)
                ));
        return new Link(uriTemplate, link.getRel());
    }

    private void addFilterToLinks(PatientIdLabelConfigSearchFilter filter) {
        this.searchFilter = filter;
    }

}
