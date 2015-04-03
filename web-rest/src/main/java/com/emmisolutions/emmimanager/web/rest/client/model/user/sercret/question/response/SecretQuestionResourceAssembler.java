package com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientSecretQuestionResponsesResource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a SecretQuestionResource from a SecretQuestion
 */
@Component("secretQuestionResourceAssembler")
public class SecretQuestionResourceAssembler implements
        ResourceAssembler<SecretQuestion, SecretQuestionResource> {

    @Override
    public SecretQuestionResource toResource(
            SecretQuestion entity) {
        SecretQuestionResource ret = new SecretQuestionResource();
        ret.add(linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).secretQuestions(null, null, null)).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
      
}
