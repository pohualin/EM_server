package com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of SecretQuestionResource objects.
 */
@XmlRootElement(name = "secretQuestionPage")
public class SecretQuestionPage extends PagedResource<SecretQuestionResource> {

    public SecretQuestionPage() {
    }

    /**
     * Wrapper for secret question resource objects
     *
     * @param secretQuestionResource to be wrapped
     * @param secretQuestionPage true page
     */
    public SecretQuestionPage(
        PagedResources<SecretQuestionResource> secretQuestionResource, 
        Page<SecretQuestion> secretQuestionPage) {
        pageDefaults(secretQuestionResource, secretQuestionPage);
    }

     
  
}
