package com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response;

import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS wrapper for a page of UserClientSecretQuestionResource objects.
 */
@XmlRootElement(name = "secret-question-response-page")
public class UserClientSecretQuestionResponsePage extends PagedResource<UserClientSecretQuestionResponseResource> {

    public UserClientSecretQuestionResponsePage() {
    }

    /**
     * Wrapper for user client secret question resource objects
     *
     * @param secretQuestionResource to be wrapped
     * @param secretQuestionPage true page
     */
    public UserClientSecretQuestionResponsePage(
        PagedResources<UserClientSecretQuestionResponseResource> secretQuestionResource, 
        Page<UserClientSecretQuestionResponse> secretQuestionPage) {
        pageDefaults(secretQuestionResource, secretQuestionPage);
    }

     
  
}
