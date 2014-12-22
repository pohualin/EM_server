package com.emmisolutions.emmimanager.web.rest.model.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;

/**
 * Creates a UserResource from a User
 */
@Component
public class SimpleUserResourceAssembler implements ResourceAssembler<User, UserResource> {

	 @Override
	 public UserResource toResource(User user) {
	        UserResource ret = new UserResource(
	                user.getId(),
	                user.getVersion(),
	                user.getLogin(),
	                user.getFirstName(),
	                user.getLastName(),
	                user.getEmail(), null);
	    ret.add(linkTo(methodOn(UsersResource.class).get(user.getId())).withSelfRel());
	    return ret;
	 }
}
