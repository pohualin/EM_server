package com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response;

import com.emmisolutions.emmimanager.model.SecretQuestion;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;


/**
 * Creates a UserClientSecretQuestionResponsesResource from a UserClientSecretQuestionResponses
 */
@Component
public class SecretQuestionResourceAssembler implements
        ResourceAssembler<SecretQuestion, SecretQuestionResource> {

    @Override
    public SecretQuestionResource toResource(
            SecretQuestion entity) {
        SecretQuestionResource ret = new SecretQuestionResource();
        ret.setEntity(entity);
        return ret;
    }
      
}
