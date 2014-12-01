package com.emmisolutions.emmimanager.web.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientResource;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientResourceAssembler;

/**
 * Users Client REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
		APPLICATION_XML_VALUE })
public class UsersClientResource {

	@Resource
	ClientService clientService;

	@Resource
	UserClientService userClientService;

	@Resource
	UserClientResourceAssembler userClientResourceAssembler;
	
	@RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.GET, consumes = {
			APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
	@RolesAllowed({ "PERM_GOD", "PERM_USER_CREATE" })
	public ResponseEntity<UserClientResource> getUsers(
			@PathVariable Long clientId) {
		return null;
	}

	/**
	 * POST to create a new UserClient
	 *
	 * @param UserClient
	 *            to create
	 * @return UserClientResource or INTERNAL_SERVER_ERROR if it could not be
	 *         created
	 * 
	 *         TODO User create permission
	 * 
	 */
	@RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.POST, consumes = {
			APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
	@RolesAllowed({ "PERM_GOD", "PERM_USER_CREATE" })
	public ResponseEntity<UserClientResource> createUser(
			@PathVariable Long clientId, @RequestBody UserClient userClient) {
		Client client = new Client();
		client.setId(clientId);
		client = clientService.reload(client);

		userClient.setClient(client);
		userClient = userClientService.save(userClient);
		if (userClient == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(
					userClientResourceAssembler.toResource(userClient),
					HttpStatus.CREATED);
		}
	}

}
