package com.emmisolutions.emmimanager.web.rest.admin.model.configuration;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientRestrictConfigurationsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.EmailRestrictConfigurationsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.IpRestrictConfigurationsResource;

/**
 * Creates a ClientRestrictConfigurationResource from a
 * ClientRestrictConfiguration
 */
@Component
public class ClientRestrictConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientRestrictConfiguration, ClientRestrictConfigurationResource> {

    /**
     * Compose ClientRestrictConfigurationResource to return
     */
    @Override
    public ClientRestrictConfigurationResource toResource(
            ClientRestrictConfiguration entity) {
        ClientRestrictConfigurationResource ret = new ClientRestrictConfigurationResource();
        ret.add(linkTo(
                methodOn(ClientRestrictConfigurationsResource.class).get(
                        entity.getId())).withSelfRel());
        ret.add(createEmailRestrictConfigLink(entity));
        ret.add(createIpRestrictConfigLink(entity));
        ret.setEntity(entity);
        return ret;
    }

    /**
     * Create full link to get EmailRestrictConfiguration
     * 
     * @param entity
     *            to use
     * @return a link to get EmailRestrictConfiguration
     */
    public static Link createEmailRestrictConfigLink(
            ClientRestrictConfiguration entity) {
        Link link = linkTo(
                methodOn(EmailRestrictConfigurationsResource.class).list(
                        entity.getId(), null, null, null)).withRel(
                "emailRestrictConfiguration");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page",
                                TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable(
                                "sort",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

    /**
     * Create full link to get IpRestrictConfiguration
     * 
     * @param entity
     *            to use
     * @return a link to get IpRestrictConfiguration
     */
    public static Link createIpRestrictConfigLink(
            ClientRestrictConfiguration entity) {
        Link link = linkTo(
                methodOn(IpRestrictConfigurationsResource.class).list(
                        entity.getId(), null, null, null)).withRel(
                "ipRestrictConfiguration");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page",
                                TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable(
                                "sort",
                                TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }
}
