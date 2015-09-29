package com.emmisolutions.emmimanager.web.rest.client.model.api;

import com.emmisolutions.emmimanager.web.rest.admin.resource.InternationalizationResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static com.emmisolutions.emmimanager.web.rest.client.resource.TrackingEmailsResource.TRACKING_TOKEN_REQUEST_PARAM;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * The public API for this server
 */
@XmlRootElement(name = "client-public")
public class ClientFacingPublicApi extends ResourceSupport {

    /**
     * create all the common links to the app
     */
    public ClientFacingPublicApi() {
        Link self = linkTo(ApiResource.class).withSelfRel();
        add(self);
        add(linkTo(methodOn(UserClientsResource.class).authenticated()).withRel("authenticated"));
        add(new Link(self.getHref() + "/authenticate", "authenticate"));
        add(new Link(self.getHref() + "/logout", "logout"));
        add(linkTo(methodOn(UserClientsPasswordResource.class)
                .changeExpiredPassword(null)).withRel("expiredPassword"));
        add(linkTo(methodOn(UserClientsActivationResource.class).activate(null)).withRel("activate"));
        add(linkTo(methodOn(UserClientsPasswordResource.class).resetPassword(null)).withRel("resetPassword"));
        add(linkTo(methodOn(UserClientsPasswordResource.class).forgotPassword(null)).withRel("forgotPassword"));
        Link resetPasswordPolicy = linkTo(methodOn(UserClientsPasswordResource.class)
                .resetPasswordPolicy(null)).withRel("resetPasswordPolicy");
        add(new Link(urlWithTokenParameter(resetPasswordPolicy), resetPasswordPolicy.getRel()));
        Link activationPasswordPolicy = linkTo(methodOn(UserClientsPasswordResource.class)
                .activatePasswordPolicy(null)).withRel("activationPasswordPolicy");
        add(new Link(urlWithTokenParameter(activationPasswordPolicy), activationPasswordPolicy.getRel()));
        add(linkTo(methodOn(InternationalizationResource.class).createStringsForLanguage(null)).withRel("messages"));
        add(linkTo(methodOn(UserClientSecretQuestionResponsesResource.class)
                .secretQuestions(null)).withRel("secretQuestions"));

        Link validateEmail = linkTo(methodOn(UserClientsResource.class)
                .validateEmailToken(null, null)).withRel("validateEmailToken");
        add(new Link(new UriTemplate(validateEmail.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable(TRACKING_TOKEN_REQUEST_PARAM, REQUEST_PARAM)
                )).toString(), validateEmail.getRel()
        ));

        Link getSecretQuestionWithResetToken = linkTo(methodOn(UserClientSecretQuestionResponsesResource.class)
                .getSecretQuestionWithResetToken(null, null, null)).withRel("getSecretQuestionWithResetToken");
        add(new Link(urlWithTokenParameter(getSecretQuestionWithResetToken), getSecretQuestionWithResetToken.getRel()));

        Link validateSecretQuestion = linkTo(methodOn(UserClientSecretQuestionResponsesResource.class)
                .validateSecretResponses(null, null, null)).withRel("validateSecurityResponse");
        add(new Link(new UriTemplate(validateSecretQuestion.getHref())
                .with(new TemplateVariables(
                                new TemplateVariable("token", REQUEST_PARAM),
                                new TemplateVariable(TRACKING_TOKEN_REQUEST_PARAM, REQUEST_PARAM_CONTINUED))
                ).toString(), validateSecretQuestion.getRel()));

        Link lockedOutUserByResetToken = linkTo(methodOn(UserClientsResource.class)
                .lockOutUserWithResetToken(null)).withRel("lockOutUserWithResetToken");
        add(new Link(urlWithTokenParameter(lockedOutUserByResetToken), lockedOutUserByResetToken.getRel()));

        add(linkTo(methodOn(PatientsResource.class).getReferenceData()).withRel("patientReferenceData"));
    }

    private String urlWithTokenParameter(Link link) {
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("token", TemplateVariable.VariableType.REQUEST_PARAM)));
        return uriTemplate.toString();
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
