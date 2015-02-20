package com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response;

import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientSecretQuestionResponsesResource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserClientSecretQuestionResponsesResource from a UserClientSecretQuestionResponses
 */
@Component
public class UserClientSecretQuestionResponseResourceAssembler implements
        ResourceAssembler<UserClientSecretQuestionResponse, UserClientSecretQuestionResponseResource> {

    @Override
    public UserClientSecretQuestionResponseResource toResource(
            UserClientSecretQuestionResponse entity) {
        UserClientSecretQuestionResponseResource ret = new UserClientSecretQuestionResponseResource();
        ret.add(linkTo(methodOn(UserClientSecretQuestionResponsesResource.class).get(entity.getUserClient().getId(), entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
      
}
