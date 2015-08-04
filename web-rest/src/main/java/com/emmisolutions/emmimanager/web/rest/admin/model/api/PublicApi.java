package com.emmisolutions.emmimanager.web.rest.admin.model.api;

import com.emmisolutions.emmimanager.web.rest.admin.model.team.TeamResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg.LanguageResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.resource.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_CAS;
import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_PRODUCTION;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * The public API for this server
 */
@XmlRootElement(name = "public")
@Component
public class PublicApi extends ResourceSupport {

    @Value("${client.application.entry.point:/client.html}")
    String clientEntryPoint;

    @XmlAttribute
    Boolean production;

    @Value("${cas.server.logout.url:https://devcas1.emmisolutions.com/cas/logout}")
    private String casServerLogoutUrl;

    @Resource
    Environment env;

    /**
     * Makes a public api from the current request
     *
     * @return a new instance of this class with the proper links
     */
    public PublicApi create() {
        PublicApi me = new PublicApi();
        Link self = linkTo(ApiResource.class).withSelfRel();
        me.add(self);
        me.add(linkTo(methodOn(UsersResource.class).authenticated()).withRel("authenticated"));
        me.add(new Link(self.getHref() + "/authenticate", "authenticate"));
        me.add(new Link(self.getHref() + "/logout", "logout"));
        me.add(new Link(
                UriComponentsBuilder.fromHttpUrl(self.getHref())
                        .replacePath(clientEntryPoint)
                        .build(false)
                        .toUriString(), "clientAppEntryUrl"));
        if (env.acceptsProfiles(SPRING_PROFILE_CAS, SPRING_PROFILE_PRODUCTION)) {
            // add location to redirect to after logout
            me.add(new Link(casServerLogoutUrl, "redirectOnLogout"));
        }
        if (env.acceptsProfiles(SPRING_PROFILE_PRODUCTION)){
            me.production = true;
        }
        me.add(linkTo(methodOn(InternationalizationResource.class).createStringsForLanguage(null)).withRel("messages"));
        me.add(linkTo(methodOn(PatientSelfRegConfigurationsResource.class).getReferenceData()).withRel("patientSelfRegReferenceData"));
        me.add(LanguageResourcePage.getAllLanguagesLink(
                linkTo(methodOn(PatientSelfRegConfigurationsResource.class)
                        .getAllAvailableLanguages(null, null)).withRel("languages")));
        me.add(TeamResourceAssembler.createTranslationsLink());
        return me;
    }

    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
